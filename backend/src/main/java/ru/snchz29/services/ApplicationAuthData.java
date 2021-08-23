package ru.snchz29.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationAuthData {
    @Value("${auth.APP_ID}")
    private Integer appId;
    @Value("${auth.SECURE_KEY}")
    private String secureKey;
    @Value("${auth.SERVICE_TOKEN}")
    private String serviceToken;
}
