package ru.snchz29.controllers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.snchz29.services.ApiClient;
import ru.snchz29.services.FriendshipGraph.FriendshipGraph;
import ru.snchz29.services.ResponseGenerator;

import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {
    private final ApiClient apiClient;
    private final FriendshipGraph friendshipGraph;
    private final ResponseGenerator generator;

    public MainController(ApiClient apiClient,
                          @Qualifier("friendshipGraphImplWithDB") FriendshipGraph friendshipGraph,
                          ResponseGenerator generator) {
        this.apiClient = apiClient;
        this.friendshipGraph = friendshipGraph;
        this.generator = generator;
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
                return generator.writeResult(friendshipGraph.getResult(), apiClient.isLoggedIn());
            } catch (ClientException | ApiException | IOException e) {
                e.printStackTrace();
            }
        return index();
    }

    @GetMapping("/login")
    public String login(@RequestParam("code") String code) {
        apiClient.login(code);
        return index();
    }

    @GetMapping("/logout")
    public String logout() {
        apiClient.logout();
        return index();
    }

    @GetMapping("/refresh")
    public String refresh() {
        try {
            return generator.writeResult(friendshipGraph.getResult(), apiClient.isLoggedIn());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generator.writeError(500, "Error while updating results");
    }
}