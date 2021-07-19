package ru.snchz29.services;

import com.google.common.collect.*;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Value;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.models.Person;

import java.util.*;
import java.util.stream.Collectors;


public class FriendshipGraph {
    private final ApiClient apiClient;
    private final PersonDAO personDAO;
    @Value("${graph.width}")
    private Integer width;
    Map<Integer, List<Integer>> graph;
    Map<Integer, Person> people;

    public FriendshipGraph(ApiClient apiClient, PersonDAO personDAO) {
        this.apiClient = apiClient;
        this.personDAO = personDAO;
        initGraph();
        initPeople();
    }

    private void initGraph() {
        List<Person> users = personDAO.getAllPersons();
        graph = users.stream().collect(Collectors.toMap(Person::getId, Person::getFriends));
    }

    private void initPeople() {
        List<Person> users = personDAO.getAllPersons();
        people = users
                .stream()
                .collect(Collectors.toMap(Person::getId, (instance) -> instance));
    }

    private Map<Integer, List<Integer>> getFriendsGraphRecursion(int depth,
                                                                 Integer id)
            throws ClientException, ApiException, InterruptedException {
        if (depth == 0 || apiClient.isUserNotValid(id)) {
            return graph;
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

    public Multimap<Person, Person> findHiddenFriends(Integer seed, int depth) throws ClientException, ApiException, InterruptedException {
        graph = getFriendsGraphRecursion(depth, seed);
        Comparator<Person> personComparator = (Comparator<Person>) (lhs, rhs) -> {
            if (lhs == rhs)
                return 0;
            if (lhs == null)
                return -1;
            if (rhs == null)
                return 1;
            return (lhs.getLastName() + lhs.getFirstName()).compareTo(rhs.getLastName() + rhs.getFirstName());
        };
        Multimap<Person, Person> result = TreeMultimap.create(personComparator, personComparator);
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
        return result;
    }
}