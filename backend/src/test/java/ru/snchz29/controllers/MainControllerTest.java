package ru.snchz29.controllers;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainControllerTest {
    @Autowired
    private MainController controller;
    // Should be in vkAuth.properties
    @Value("${test.vk_id}")
    private Integer vkId;
    // Should be in vkAuth.properties
    @Value("${test.access_token}")
    private String access_token;

    @SneakyThrows
    @Test
    void index() {
        String responseString = controller.index();
        assertThat(responseString).contains("isLoggedIn");
        String loginLink = getLoginLink();
        assertThat(loginLink).matches("https://oauth\\.vk\\.com/authorize\\?client_id=[0-9]*&display=popup&redirect_uri=http://localhost:8080/login&scope=friends,groups,photos&response_type=code&v=5\\.120");
    }

    @SneakyThrows
    String getLoginLink() {
        String responseString = controller.index();
        JSONObject response = new JSONObject(responseString);
        if (!response.getBoolean("isLoggedIn"))
            return response.getString("loginLink");
        else
            return null;
    }

    @SneakyThrows
    @Test
    void login() {
        controller.auth(vkId, access_token);
        String responseString = controller.index();
        JSONObject response = new JSONObject(responseString);
        assertThat(response.getBoolean("isLoggedIn")).isTrue();
    }
}