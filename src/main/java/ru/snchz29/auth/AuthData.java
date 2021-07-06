package ru.snchz29.auth;

import org.springframework.beans.factory.annotation.Value;

public class AuthData {
    @Value("${APP_ID}")
    private Integer appId;
    @Value("${SECURE_KEY}")
    private String secureKey;
    @Value("${SERVICE_TOKEN}")
    private String serviceToken;
    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
