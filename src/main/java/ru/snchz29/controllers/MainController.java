package ru.snchz29.controllers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.snchz29.models.Person;
import ru.snchz29.workers.FriendshipGraphHandler;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {
    private final FriendshipGraphHandler friendshipGraphHandler;

    public MainController(FriendshipGraphHandler friendshipGraphHandler) {
        this.friendshipGraphHandler = friendshipGraphHandler;
    }

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/run/{id}")
    public String run(Model model, @PathVariable("id") int id) {
        try {
            Map<Person, List<Person>> result = friendshipGraphHandler.findHiddenFriends(id);
            model.addAttribute("result", result);
        } catch (ClientException | InterruptedException | ApiException e) {
            e.printStackTrace();
        }
        return "result";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
