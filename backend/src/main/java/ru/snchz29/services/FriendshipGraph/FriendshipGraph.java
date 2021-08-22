package ru.snchz29.services.FriendshipGraph;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.scheduling.annotation.Async;
import ru.snchz29.models.Person;

import java.util.concurrent.CompletableFuture;

public interface FriendshipGraph {
    @Async
    CompletableFuture<Integer> findHiddenFriends(Integer seed, int depth, int width) throws ClientException, ApiException;

    Multimap<Person, Person> getResult();
}
