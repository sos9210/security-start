package com.ssh.security1.config.oauth.provider;

//여러 Provider 유저정보를 받기위한 인터페이스
public interface OAuth2Userinfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
