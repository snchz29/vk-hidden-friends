package ru.snchz29.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.snchz29.workers.ApiClient;
import ru.snchz29.workers.FriendshipGraphHandler;
import ru.snchz29.dao.PersonDAO;
import ru.snchz29.auth.AuthData;

import javax.sql.DataSource;


@Configuration
@ComponentScan("ru.snchz29")
@PropertySource("classpath:app.properties")
public class SpringConfig {
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/vk-hidden-friends");
        dataSource.setUsername("postgres");
        dataSource.setPassword("12345678");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PersonDAO personDAO(){
        return new PersonDAO(jdbcTemplate());
    }

    @Bean
    public FriendshipGraphHandler friendshipGraphHandler() {
        return new FriendshipGraphHandler(apiClient(), personDAO(), 20);
    }

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(vkApiClient(), userActor(), serviceActor());
    }

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(new HttpTransportClient());
    }

    @Bean
    public UserActor userActor() {
        return new UserActor(authData().getAppId(), authData().getAccessToken());
    }

    @Bean
    public ServiceActor serviceActor() {
        return new ServiceActor(authData().getAppId(), authData().getSecureKey(), authData().getServiceToken());
    }

    @Bean
    public AuthData authData() {
        return new AuthData();
    }
}
