package com.ssh.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //구글로부터 받은 userRequest 데이터에 대한 후처리 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //registrationId로 어떤 OAuth로 로그인했는지 확인가능
        System.out.println("userRequest.getClientRegistration(): " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken().getTokenValue(): " + userRequest.getAccessToken().getTokenValue());
        
        //userRequest 정보 -> loadUser 메서드 호출 -> 회원프로필
        System.out.println("super.loadUser(userRequest).getAttributes(): " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
