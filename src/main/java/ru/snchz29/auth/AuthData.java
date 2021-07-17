package ru.snchz29.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class AuthData {
    @Value("${auth.APP_ID}")
    private Integer appId;
    @Value("${auth.SECURE_KEY}")
    private String secureKey;
    @Value("${auth.SERVICE_TOKEN}")
    private String serviceToken;
    @Value("${auth.ACCESS_TOKEN}")
    private String accessToken;
}
