package ru.snchz29.services;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.snchz29.models.Person;
import ru.snchz29.services.FriendshipGraph.FriendshipGraph;

import java.util.concurrent.CompletableFuture;

@Service
public class SessionFacade {
    private final ApiClient apiClient;
    private final FriendshipGraph friendshipGraph;
    private boolean isRunning;
    private CompletableFuture<Integer> asyncStatus;

    public SessionFacade(ApiClient apiClient, @Qualifier("friendshipGraphImplWithDB") FriendshipGraph friendshipGraph) {
        this.apiClient = apiClient;
        this.friendshipGraph = friendshipGraph;
    }

    public void login(String code) {
        apiClient.login(code);
    }

    public void logout() {
        apiClient.logout();
    }

    public boolean isLoggedIn() {
        return apiClient.isLoggedIn();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run(int id, int depth, int width) {
        if (!isRunning)
            try {
                asyncStatus = friendshipGraph.findHiddenFriends(id, depth, width);
                isRunning = true;
                asyncStatus.thenAccept((result) -> isRunning = false);
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            }
    }

    public void stop() {
        if (isRunning) {
            asyncStatus.cancel(true);
            isRunning = false;
        }
    }

    public Multimap<Person, Person> getResult() {
        return friendshipGraph.getResult();
    }
}
