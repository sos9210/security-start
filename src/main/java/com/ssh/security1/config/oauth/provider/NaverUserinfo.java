package com.ssh.security1.config.oauth.provider;

import java.util.Map;

public class NaverUserinfo implements OAuth2Userinfo{
    private Map<String,Object> attributes; //getAttributes()

    public NaverUserinfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
