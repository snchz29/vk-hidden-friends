package ru.snchz29.controllers;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.snchz29.models.Person;
import ru.snchz29.models.ResultEntry;
import ru.snchz29.services.ApiClient;
import ru.snchz29.services.FriendshipGraph.FriendshipGraph;
import ru.snchz29.services.ResponseGeneratorWrapper;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class MainController {
    private final ApiClient apiClient;
    private final FriendshipGraph friendshipGraph;
    @Value("${controller.frontendURL}")
    private String frontendURL;

    public MainController(ApiClient apiClient,
                          @Qualifier("friendshipGraphImplWithDB") FriendshipGraph friendshipGraph) {
        this.apiClient = apiClient;
        this.friendshipGraph = friendshipGraph;
    }

    @GetMapping()
    public String index() {
        JSONObject response = new JSONObject().put("isLoggedIn", apiClient.isLoggedIn());
        if (!apiClient.isLoggedIn()) {
            response.put("loginLink", "https://oauth.vk.com/authorize?client_id=7900610&display=popup&redirect_uri=http://localhost:8080/login&scope=friends,groups,photos&response_type=code&v=5.120");
        }
        return response.toString();
    }

    @GetMapping("/result")
    public String result() {
        return index();
    }

    @GetMapping("/result/{id}")
    public String result(@PathVariable("id") int id, @PathParam("depth") int depth, @PathParam("width") int width) {
        if (apiClient.isLoggedIn())
            try {
                friendshipGraph.findHiddenFriends(id, depth, width);
                return generateJSON(friendshipGraph.getResult(), apiClient.isLoggedIn());
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            }
        return index();
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("code") String code) {
        apiClient.login(code);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendURL)).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        apiClient.logout();
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendURL)).build();
    }

    @GetMapping("/refresh")
    public String refresh() {
        return generateJSON(friendshipGraph.getResult(), apiClient.isLoggedIn());
    }

    @SneakyThrows
    private String generateJSON(Multimap<Person, Person> result, boolean loginState) {
        List<ResultEntry> resultAsList = result
                .keySet()
                .stream()
                .collect(
                        LinkedList::new,
                        (list, user) -> list.add(new ResultEntry(user, result.get(user))),
                        LinkedList::addAll
                );
        return ResponseGeneratorWrapper.json().writeStartObject()
                .writeBooleanField("isLoggedIn", loginState)
                .writeObjectArray("result", Collections.singletonList(resultAsList))
                .writeEndObject()
                .close()
                .toString();
    }
}
