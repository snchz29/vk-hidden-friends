package ru.snchz29.controllers;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.snchz29.models.Person;
import ru.snchz29.services.ApiClient;
import ru.snchz29.services.FriendshipGraph;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class MainController {
    private final ApiClient apiClient;
    private final FriendshipGraph friendshipGraph;

    public MainController(ApiClient apiClient, FriendshipGraph friendshipGraph) {
        this.apiClient = apiClient;
        this.friendshipGraph = friendshipGraph;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("loggedIn", apiClient.isLoggedIn());
        return "start";
    }

    @GetMapping("/result/{id}")
    public String result(Model model, @PathVariable("id") int id) {
        try {
            Multimap<Person, Person> result = friendshipGraph.findHiddenFriends(id, 0);
            model.addAttribute("loggedIn", apiClient.isLoggedIn());
            model.addAttribute("result", result);
            return "result";
        } catch (ClientException | InterruptedException | ApiException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@RequestParam("code") String code) {
        apiClient.login(code);
        return "redirect:/";
    }
}
