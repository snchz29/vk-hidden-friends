package ru.snchz29.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Service;
import ru.snchz29.models.Person;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class SimpleFriendshipGraphImpl extends FriendshipGraphImpl{

    public SimpleFriendshipGraphImpl(ApiClient apiClient) {
        super(apiClient);
        this.graph = new HashMap<>();
        this.people = new HashMap<>();
    }

    @Override
    Person getPerson(Integer id) throws ClientException, ApiException {
        Person person = people.get(id);
        if (person != null) {
            return person;
        }
        Person user = apiClient.getUsers(Collections.singletonList(id)).get(0);
        user.setFriends(apiClient.getUserFriendsIds(id));
        return user;
    }

    @Override
    List<Integer> getFriends(Integer id) throws ClientException, ApiException {
        List<Integer> friends = graph.get(id);
        if (friends == null) {
            friends = apiClient.getUserFriendsIds(id);
        }
        return friends;
    }
}
