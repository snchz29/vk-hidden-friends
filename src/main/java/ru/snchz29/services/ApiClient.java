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
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.snchz29.models.Person;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final int TIMEOUT = 400;
    private final AuthData authData;
    private final VkApiClient apiClient;
    private UserActor userActor;

    public ApiClient(AuthData authData) {
        this.authData = authData;
        this.apiClient = new VkApiClient(new HttpTransportClient());
    }

    public List<Integer> getUserFriendsIds(Integer id) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        logger.info("Finding friends of " + id);
        return apiClient.friends().get(userActor).userId(id).execute().getItems();
    }

    public List<Person> getUsers(List<Integer> id) throws InterruptedException, ClientException {
        Thread.sleep(TIMEOUT);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = apiClient
                .users()
                .get(userActor)
                .lang(Lang.RU)
                .userIds(id
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()))
                .fields(Fields.PHOTO_400)
                .executeAsString();
        try {
            String jsonArray = new JSONObject(response).getJSONArray("response").toString();
            List<Person> people = objectMapper.readValue(jsonArray, new TypeReference<List<Person>>() {
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

    public boolean isUserNotValid(Integer id) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
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

    public List<Photo> getUserAvatars(Integer userId) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        return apiClient.photos().get(userActor)
                .ownerId(userId).albumId("profile")
                .execute().getItems();
    }

    public void login(String code) {
        try {
            UserAuthResponse authResponse = apiClient.oAuth()
                    .userAuthorizationCodeFlow(authData.getAppId(), authData.getSecureKey(), "http://localhost:8080/login", code)
                    .execute();
            if (authResponse != null) {
                userActor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
                logger.info("Successful login for user " + authResponse.getUserId());
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return userActor != null;
    }
}
