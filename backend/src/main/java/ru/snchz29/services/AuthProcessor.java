package ru.snchz29.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthProcessor {
    private static final Logger logger = LogManager.getLogger(AuthProcessor.class);
    private final ApplicationAuthData applicationAuthData;
    private UserActor userActor;

    public AuthProcessor(ApplicationAuthData applicationAuthData) {
        this.applicationAuthData = applicationAuthData;
    }

    public void login(String code) {
        try {
            VkApiClient apiClient = new VkApiClient(new HttpTransportClient());
            UserAuthResponse authResponse = apiClient.oAuth()
                    .userAuthorizationCodeFlow(applicationAuthData.getAppId(), applicationAuthData.getSecureKey(), "http://localhost:8080/login", code)
                    .execute();
            if (authResponse != null) {
                setUserActor(authResponse.getUserId(), authResponse.getAccessToken());
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void setUserActor(Integer userId, String accessToken) {
        userActor = new UserActor(userId, accessToken);
        logger.info("Successful login for user " + userId);
    }

    public boolean isLoggedIn() {
        return userActor != null;
    }

    public void logout() {
        logger.info("Logout");
        userActor = null;
    }


    public UserActor getUserActor() {
        return userActor;
    }
}
