package ru.snchz29.services;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.scheduling.annotation.Async;
import ru.snchz29.models.Person;

public interface FriendshipGraph {
    @Async
    void findHiddenFriends(Integer seed, int depth) throws ClientException, ApiException, InterruptedException;

    Multimap<Person, Person> getResult();
}
