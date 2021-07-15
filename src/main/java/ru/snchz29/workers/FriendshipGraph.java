package ru.snchz29.workers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.models.Person;

import java.util.*;
import java.util.stream.Collectors;


public class FriendshipGraph {
    private final ApiClient apiClient;
    private final PersonDAO personDAO;
    private final double width;
    Map<Integer, List<Integer>> graph;
    Map<Integer, Person> people;

    public FriendshipGraph(ApiClient apiClient, PersonDAO personDAO, double width) {
        this.apiClient = apiClient;
        this.personDAO = personDAO;
        this.width = width;
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
            if (Math.random() < depth * width / friends.size()) {
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
            personDAO.savePerson(
                    new Person(id,
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhotoUri(),
                            friends
                    ));
        }
        return friends;
    }

    private Person getPerson(Integer id) throws ClientException, InterruptedException, ApiException {
        if (people.containsKey(id)) {
            return people.get(id);
        }
        Person person = personDAO.getPerson(id);
        if (person != null) {
            return person;
        }
        List<String> fullName = Arrays.asList(apiClient.getName(id).split(" "));
        return new Person(id,
                fullName.get(0),
                fullName.get(fullName.size() - 1),
                apiClient.getAvatarURL(id),
                apiClient.getUserFriendsIds(id)
        );
    }

    public Map<Person, List<Person>> findHiddenFriends(Integer seed, int depth) throws ClientException, ApiException, InterruptedException {
        graph = getFriendsGraphRecursion(depth, seed);

        Map<Person, List<Person>> result = new HashMap<>();
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

                if (!result.containsKey(hidPerson)) {
                    result.put(hidPerson, new LinkedList<>());
                }
                result.get(hidPerson).add(hiddenPerson);
            }
        }
        return result;
    }
}