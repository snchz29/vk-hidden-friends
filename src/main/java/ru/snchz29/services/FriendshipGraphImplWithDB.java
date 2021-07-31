package ru.snchz29.services;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.models.Person;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendshipGraphImplWithDB implements FriendshipGraph{
    private final ApiClient apiClient;
    private final PersonDAO personDAO;
    private Map<Integer, List<Integer>> graph;
    private Map<Integer, Person> people;
    private Multimap<Person, Person> result;
    @Value("${graph.width}")
    private Integer width;

    public FriendshipGraphImplWithDB(ApiClient apiClient, PersonDAO personDAO) {
        this.apiClient = apiClient;
        this.personDAO = personDAO;
        this.result = TreeMultimap.create(Person.comparator, Person.comparator);
        initPeople();
        initGraph();
    }

    @Async
    public void findHiddenFriends(Integer seed, int depth) throws ClientException, ApiException, InterruptedException {
        graph = getFriendsGraphRecursion(depth, seed);
    }

    public Multimap<Person, Person> getResult() {
        return result;
    }

    @SneakyThrows
    private void updateResult() {
        for (Integer hiddenId : graph.keySet()) {
            for (Integer hidId : graph.get(hiddenId)) {
                if (graph.get(hidId) == null)
                    continue;
                if (graph.get(hidId).contains(hiddenId))
                    continue;
                if (!graph.get(hiddenId).contains(hidId))
                    continue;

                Person hiddenPerson = getPerson(hiddenId);
                Person hidPerson = getPerson(hidId);
                result.put(hidPerson, hiddenPerson);
            }
        }
    }

    private void initGraph() {
        List<Person> users = personDAO.getAllPersons();
        graph = users.stream().collect(Collectors.toMap(Person::getId, Person::getFriends));
        updateResult();
    }

    private void initPeople() {
        List<Person> users = personDAO.getAllPersons();
        people = users
                .stream()
                .collect(Collectors.toMap(Person::getId, (instance) -> instance));
    }

    private Map<Integer, List<Integer>> getFriendsGraphRecursion(int depth, Integer id)
            throws ClientException, ApiException, InterruptedException {
        if (depth == 0 || apiClient.isUserNotValid(id)) {
            return graph;
        }
        if (graph.keySet().size() % 40 == 0) {
            updateResult();
        }
        List<Integer> friends = getFriends(id);
        graph.put(id, friends);
        for (Integer friend : friends) {
            if (Math.random() < depth * width / (double) friends.size()) {
                getFriendsGraphRecursion(depth - 1, friend);
            }
        }
        return graph;
    }

    private List<Integer> getFriends(Integer id) throws ClientException, ApiException, InterruptedException {
        List<Integer> friends = graph.get(id);
        if (friends == null) {
            friends = apiClient.getUserFriendsIds(id);
            Person user = apiClient.getUsers(Collections.singletonList(id)).get(0);
            user.setFriends(friends);
            personDAO.savePerson(user);
        }
        return friends;
    }

    private Person getPerson(Integer id) throws ClientException, InterruptedException, ApiException {
        Person person = people.get(id);
        if (person != null) {
            return person;
        }
        person = personDAO.getPerson(id);
        if (person != null) {
            return person;
        }
        Person user = apiClient.getUsers(Collections.singletonList(id)).get(0);
        user.setFriends(apiClient.getUserFriendsIds(id));
        return user;
    }
}