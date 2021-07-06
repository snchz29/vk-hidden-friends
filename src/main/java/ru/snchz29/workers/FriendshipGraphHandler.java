package ru.snchz29.workers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.models.Person;

import java.util.*;
import java.util.stream.Collectors;


public class FriendshipGraphHandler {
    private final ApiClient apiClient;
    private final PersonDAO personDAO;
    private final double width;
    Map<Integer, List<Integer>> graph;
    Map<Integer, String> names;

    public FriendshipGraphHandler(ApiClient apiClient, PersonDAO personDAO, double width) {
        this.apiClient = apiClient;
        this.personDAO = personDAO;
        this.width = width;
        initGraph();
        initNames();
    }

    private void initGraph() {
        List<Person> users = personDAO.getAllPersons();
        graph = users.stream().collect(Collectors.toMap(Person::getId, Person::getFriends));
    }

    private void initNames() {
        List<Person> users = personDAO.getAllPersons();
        names = users
                .stream()
                .collect(Collectors.toMap(Person::getId, (p) -> p.getFirstName() + " " + p.getLastName()));
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

        if (!graph.containsKey(id)) {
            friends = apiClient.getUserFriendsIds(id);
            String fullName = apiClient.getName(id);
            personDAO.savePerson(
                    new Person(id,
                            fullName.split(" ")[0],
                            fullName.split(" ")[1],
                            apiClient.getAvatarURL(id),
                            friends
                    ));
        }
        return friends;
    }

    private String getName(Integer id) throws ClientException, InterruptedException, ApiException {
        if (names.containsKey(id)) {
            return names.get(id);
        }
        Person person = personDAO.getPerson(id);
        if (person != null) {
            return person.getFirstName() + " " + person.getLastName() + "(" + id + ")";
        }
        return apiClient.getName(id);
    }

    public Map<String, List<String>> findHiddenFriends(Integer seed) throws ClientException, ApiException, InterruptedException {
        graph = getFriendsGraphRecursion(3, seed);
        Map<String, List<String>> result = new HashMap<>();

        for (Integer hiddenId : graph.keySet()) {
            for (Integer hidId : graph.get(hiddenId)) {
                if (graph.get(hidId) == null)
                    continue;
                if (graph.get(hidId).contains(hiddenId))
                    continue;
                if (!graph.get(hiddenId).contains(hidId))
                    continue;

                String hiddenName = getName(hiddenId);
                String hidName = getName(hidId);

                if (!result.containsKey(hiddenName)) {
                    result.put(hidName, new LinkedList<>());
                }
                result.get(hidName).add(hiddenName);

            }
        }
        return result;
    }
}