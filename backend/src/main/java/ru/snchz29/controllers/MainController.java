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
import ru.snchz29.services.SessionFacade;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "${controller.frontendURL}")
@RestController
@RequestMapping("/")
public class MainController {
    private final SessionFacade session;
    @Value("${controller.frontendURL}")
    private String frontendURL;
    @Value("${auth.loginLink}")
    private String loginLink;

    public MainController(SessionFacade sessionFacade) {
        this.session = sessionFacade;
    }

    @GetMapping()
    public String index() {
        JSONObject response = new JSONObject().put("isLoggedIn", session.isLoggedIn());
        if (!session.isLoggedIn()) {
            response.put("loginLink", loginLink);
        }
        return response.toString();
    }

    @GetMapping("/kill")
    public void kill() {
        session.stop();
    }

    @GetMapping("/result/{id}")
    public void result(@PathVariable("id") int id, @PathParam("depth") int depth, @PathParam("width") int width) {
        if (!session.isLoggedIn()) {
            return;
        }
        session.run(id, depth, width);
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("code") String code) {
        session.login(code);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendURL)).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        session.logout();
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendURL)).build();
    }

    @GetMapping("/refresh")
    public String refresh() {
        return generateJSON(session.getResult());
    }

    @SneakyThrows
    private String generateJSON(Multimap<Person, Person> result) {
        if (session.isLoggedIn())
            return ResponseGeneratorWrapper.json().writeStartObject()
                    .writeBooleanField("isLoggedIn", true)
                    .writeBooleanField("isRunning", session.isRunning())
                    .writeObjectArray("result", result
                            .keySet()
                            .stream()
                            .collect(
                                    LinkedList::new,
                                    (list, user) -> list.add(new ResultEntry(user, result.get(user))),
                                    LinkedList::addAll
                            ))
                    .writeEndObject()
                    .close()
                    .toString();
        else
            return index();
    }
}
