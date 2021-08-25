package ru.snchz29.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainControllerTest {
    @Autowired
    private MainController controller;
    private static WebDriver driver;
    public static LoginPage loginPage;
    // Should be in vkAuth.properties
    @Value("${test.login}")
    private String login;
    // Should be in vkAuth.properties
    @Value("${test.password}")
    private String password;


    static {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        loginPage = new LoginPage(driver);
    }

    @SneakyThrows
    @Test
    void index() {
        String responseString = controller.index(0, "");
        assertThat(responseString).contains("isLoggedIn");
        String loginLink = getLoginLink();
        assertThat(loginLink).matches("https://oauth\\.vk\\.com/authorize\\?client_id=[0-9]*&display=popup&redirect_uri=http://localhost:8080/login&scope=friends,groups,photos&response_type=code&v=5\\.120");
    }

    @SneakyThrows
    String getLoginLink() {
        String responseString = controller.index(0, "");
        JSONObject response = new JSONObject(responseString);
        if (!response.getBoolean("isLoggedIn"))
            return response.getString("loginLink");
        else
            return null;
    }

    @Test
    void login() {
        driver.get(getLoginLink());
        loginPage.inputLogin(login);
        loginPage.inputPassword(password);
        loginPage.clickLoginBtn();
        driver.quit();
    }
}