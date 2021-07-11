package ru.snchz29.workers;

import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ApiClient {
    private final VkApiClient apiClient;
    private final UserActor userActor;
    private final ServiceActor serviceActor;
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final int TIMEOUT = 200;

    public ApiClient(VkApiClient apiClient, UserActor userActor, ServiceActor serviceActor) {
        this.apiClient = apiClient;
        this.userActor = userActor;
        this.serviceActor = serviceActor;
    }

    public List<Integer> getUserFriendsIds(Integer id) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        logger.info("Finding friends of " + id);
        return apiClient.friends().get(serviceActor).userId(id).execute().getItems();
    }

    public boolean isUserNotValid(Integer id) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        logger.info("Check user with id: " + id);
        List<GetResponse> result = apiClient.users().get(serviceActor).userIds(String.valueOf(id)).execute();
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

    public String getName(Integer id) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        logger.info("Get name of " + id);
        if (isUserNotValid(id))
            return null;
        GetResponse user = apiClient
                .users()
                .get(serviceActor)
                .lang(Lang.RU)
                .userIds(String.valueOf(id))
                .execute().get(0);
        return user.getFirstName() + " " + user.getLastName() + "(" + id + ")";
    }

    public List<Photo> getUserAvatars(Integer userId) throws ClientException, ApiException, InterruptedException {
        Thread.sleep(TIMEOUT);
        return apiClient.photos().get(userActor)
                .ownerId(userId).albumId("profile")
                .execute().getItems();
    }

    public String getAvatarURL(Integer userId) throws ClientException, ApiException, InterruptedException {
        List<Photo> profilePhotoList = getUserAvatars(userId);
        if (profilePhotoList.size() == 0)
            return "https://vk.com/images/camera_50.png";
        Photo avatarItself = profilePhotoList.get(profilePhotoList.size() - 1);
        return avatarItself.getSizes().get(0).getUrl().toString();
    }
}
