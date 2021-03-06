package ru.snchz29.services.FriendshipGraph;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snchz29.models.Person;
import ru.snchz29.services.ApiClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

abstract class FriendshipGraphImpl implements FriendshipGraph {
    protected static final Logger logger = LogManager.getLogger(FriendshipGraphImpl.class);
    protected final ApiClient apiClient;
    protected Multimap<Person, Person> result;
    protected Map<Integer, List<Integer>> graph;
    protected Map<Integer, Person> people;

    public FriendshipGraphImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.result = TreeMultimap.create(Person.comparator, Person.comparator);
    }

    abstract Person getPerson(Integer id) throws ClientException, ApiException;

    abstract List<Integer> getFriends(Integer id) throws ClientException, ApiException;

    @Override
    public CompletableFuture<Integer> findHiddenFriends(Integer seed, int depth, int width, UserActor userActor) throws ClientException, ApiException {
        apiClient.setUserActor(userActor);
        logger.info("Search starts");
        logger.info(String.format("Search params: depth=%d width=%d", depth, width));
        graph = getFriendsGraphRecursion(depth, width, seed);
        return CompletableFuture.completedFuture(1);
    }

    @Override
    public Multimap<Person, Person> getResult() {
        return result;
    }

    protected Map<Integer, List<Integer>> getFriendsGraphRecursion(int depth, int width, Integer id)
            throws ClientException, ApiException {
        if (depth == 0 || apiClient.isUserNotValid(id)) {
            return graph;
        }
        if (graph.keySet().size() % 20 == 0) {
            logger.info("Start updating results");
            updateResult();
            logger.info("Results updated");
        }
        List<Integer> friends = getFriends(id);
        graph.put(id, friends);
        for (Integer friend : friends) {
            if (Math.random() < depth * width / (double) friends.size()) {
                getFriendsGraphRecursion(depth - 1, width, friend);
            }
        }
        return graph;
    }

    @SneakyThrows
    protected void updateResult() {
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
}
