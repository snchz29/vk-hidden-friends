package ru.snchz29.controllers;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.snchz29.auth.AuthData;
import ru.snchz29.models.Person;
import ru.snchz29.services.ApiClient;
import ru.snchz29.services.FriendshipGraph;

@Controller
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
        return "index";
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
    public String login(@RequestParam("code") String code){
        apiClient.login(code);
        return "redirect:/";
    }
}
