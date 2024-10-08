package com.ssh.security1.config.oauth.provider;

import java.util.Map;

public class GoogleUserinfo implements OAuth2Userinfo{
    private Map<String,Object> attributes; //getAttributes()

    public GoogleUserinfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
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
