package ru.snchz29.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class AuthData {
    @Value("${APP_ID}")
    private Integer appId;
    @Value("${SECURE_KEY}")
    private String secureKey;
    @Value("${SERVICE_TOKEN}")
    private String serviceToken;
    @Value("${ACCESS_TOKEN}")
    private String accessToken;
}
