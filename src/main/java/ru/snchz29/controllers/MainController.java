package ru.snchz29.controllers;

import com.google.common.collect.Multimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.snchz29.models.Person;
import ru.snchz29.services.FriendshipGraph;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {
    private final FriendshipGraph friendshipGraph;

    public MainController(FriendshipGraph friendshipGraph) {
        this.friendshipGraph = friendshipGraph;
    }

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/result/{id}")
    public String result(Model model, @PathVariable("id") int id) {
        try {
            Multimap<Person, Person> result = friendshipGraph.findHiddenFriends(id, 0);
            model.addAttribute("result", result);
        } catch (ClientException | InterruptedException | ApiException e) {
            e.printStackTrace();
        }
        return "result";
    }
}
