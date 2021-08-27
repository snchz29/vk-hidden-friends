package ru.snchz29.config;

import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.snchz29.services.ApiClient;
import ru.snchz29.services.AuthProcessor;

@Configuration
public class Config {
    private final ApplicationContext context;

    public Config(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public ApiClient apiClient() {
        return new ApiClient() {
            @Override
            protected UserActor getUserActor() {
                return context.getBean(AuthProcessor.class).getUserActor();
            }
        };
    }
}
