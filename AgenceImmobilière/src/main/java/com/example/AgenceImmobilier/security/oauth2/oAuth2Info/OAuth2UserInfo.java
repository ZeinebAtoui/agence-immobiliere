package com.example.AgenceImmobilier.security.oauth2.oAuth2Info;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
@AllArgsConstructor
@Data
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
