package ru.snchz29.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Service;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.models.Person;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipGraphImplWithDB extends FriendshipGraphImpl{
    private final PersonDAO personDAO;

    public FriendshipGraphImplWithDB(ApiClient apiClient, PersonDAO personDAO) {
        super(apiClient);
        this.personDAO = personDAO;
        initPeople();
        initGraph();
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

    protected List<Integer> getFriends(Integer id) throws ClientException, ApiException {
        List<Integer> friends = graph.get(id);
        if (friends == null) {
            friends = apiClient.getUserFriendsIds(id);
            Person user = apiClient.getUsers(Collections.singletonList(id)).get(0);
            user.setFriends(friends);
            personDAO.savePerson(user);
        }
        return friends;
    }

    @Override
    protected Person getPerson(Integer id) throws ClientException, ApiException {
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