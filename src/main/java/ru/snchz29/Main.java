package ru.snchz29;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.snchz29.config.SpringConfig;
import ru.snchz29.workers.FriendshipGraphHandler;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        FriendshipGraphHandler friendshipGraphHandler = context.getBean("friendshipGraphHandler", FriendshipGraphHandler.class);
        try {
            Map<String, List<String>> hiddenFriends = friendshipGraphHandler.findHiddenFriends(147946476);
            for (String user : hiddenFriends.keySet()) {
                for (String friend : hiddenFriends.get(user)) {
                    System.out.printf("%-50s -> %50s%n", user, friend);
                }
            }
            if (hiddenFriends.isEmpty())
                System.out.println("Can't find any hidden friends");
        } catch (ClientException | ApiException | InterruptedException e) {
            e.printStackTrace();
        }
        context.close();
    }
}
