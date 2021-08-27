package ru.snchz29.services;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.snchz29.models.Person;
import ru.snchz29.services.FriendshipGraph.FriendshipGraph;

import java.util.concurrent.CompletableFuture;

@Service
public class SessionFacade {
    private final AuthProcessor authProcessor;
    private final FriendshipGraph friendshipGraph;
    private boolean isRunning;
    private CompletableFuture<Integer> asyncStatus;

    public SessionFacade(AuthProcessor authProcessor, @Qualifier("friendshipGraphImplWithDB") FriendshipGraph friendshipGraph) {
        this.authProcessor = authProcessor;
        this.friendshipGraph = friendshipGraph;
    }

    public void setUserActor(Integer userId, String accessToken) {
        authProcessor.setUserActor(userId, accessToken);
    }

    public void login(String code) {
        authProcessor.login(code);
    }

    public void logout() {
        authProcessor.logout();
    }

    public boolean isLoggedIn() {
        return authProcessor.isLoggedIn();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run(int id, int depth, int width) {
        if (!isRunning)
            try {
                asyncStatus = friendshipGraph.findHiddenFriends(id, depth, width, authProcessor.getUserActor());
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
