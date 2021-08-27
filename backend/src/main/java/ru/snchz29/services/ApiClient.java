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
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.snchz29.models.Person;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final int TIMEOUT = 300;
    private final VkApiClient apiClient = new VkApiClient(new HttpTransportClient());
    private UserActor userActor;

    public void setUserActor(UserActor userActor){
        this.userActor = userActor;
        logger.info("For ApiClient: " + this + " set UserActor: " + userActor);
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

    @SneakyThrows
    private void timeout() {
        Thread.sleep(TIMEOUT);
    }
}
