package ru.snchz29.controllers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.snchz29.models.FriendPair;
import ru.snchz29.workers.FriendshipGraphHandler;

import java.util.LinkedList;
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
    public String run(Model model, @PathVariable("id") int id){
        try {
            Map<String, List<String>> result = friendshipGraphHandler.findHiddenFriends(id);
            List<FriendPair> pairs = new LinkedList<>();
            for (String user : result.keySet()) {
                for (String friend : result.get(user)) {
                    pairs.add(new FriendPair(user, friend));
                    System.out.println(user + " " + friend);
                }
            }
            model.addAttribute("pairs", pairs);
        } catch (ClientException | InterruptedException | ApiException e) {
            e.printStackTrace();
        }
        return "result";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
