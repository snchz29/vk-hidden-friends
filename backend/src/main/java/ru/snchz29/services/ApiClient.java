package ru.snchz29.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.snchz29.models.Person;

import java.util.List;
import java.util.stream.Collectors;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final int TIMEOUT = 300;
    private final ApplicationAuthData applicationAuthData;
    private final VkApiClient apiClient;
    private UserActor userActor;

    public ApiClient(ApplicationAuthData applicationAuthData) {
        this.applicationAuthData = applicationAuthData;
        this.apiClient = new VkApiClient(new HttpTransportClient());
    }

    public List<Integer> getUserFriendsIds(Integer id) throws ClientException, ApiException {
        timeout();
        logger.info("Finding friends of " + id);
        return apiClient.friends().get(userActor).userId(id).execute().getItems();
    }

    public List<Person> getUsers(List<Integer> ids) throws ClientException {
        String response = getJSONUsers(ids);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonArray = new JSONObject(response).getJSONArray("response").toString();
            List<Person> people = objectMapper.readValue(jsonArray, new TypeReference<>() {
            });
            for (Person person : people) {
                if (person.getPhotoUri() == null) {
                    person.setPhotoUri("https://vk.com/images/camera_50.png");
                }
            }
            return people;
        } catch (JsonProcessingException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getJSONUsers(List<Integer> ids) throws ClientException {
        timeout();
        return apiClient
                .users()
                .get(userActor)
                .lang(Lang.RU)
                .userIds(ids
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()))
                .fields(Fields.PHOTO_400)
                .executeAsString();
    }

    public boolean isUserNotValid(Integer id) throws ClientException, ApiException {
        timeout();
        logger.info("Check user with id: " + id);
        List<GetResponse> result = apiClient.users().get(userActor).userIds(String.valueOf(id)).execute();
        if (result == null)
            return true;
        GetResponse user = result.get(0);
        if (user == null)
            return true;
        try {
            return user.getIsClosed() || user.getDeactivated() != null;
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return true;
    }

    public void login(String code) {
        try {
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

    public void setUserActor(Integer user_id, String accessToken) {
        userActor = new UserActor(user_id, accessToken);
        logger.info("Successful login for user " + user_id);
    }

    public boolean isLoggedIn() {
        return userActor != null;
    }

    public void logout() {
        logger.info("Logout");
        userActor = null;
    }

    @SneakyThrows
    private void timeout() {
        Thread.sleep(TIMEOUT);
    }
}
