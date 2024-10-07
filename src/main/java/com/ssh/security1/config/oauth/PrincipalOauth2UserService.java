package com.ssh.security1.config.oauth;

import com.ssh.security1.config.auth.PrincipalDetails;
import com.ssh.security1.config.oauth.provider.GoogleUserinfo;
import com.ssh.security1.config.oauth.provider.NaverUserinfo;
import com.ssh.security1.config.oauth.provider.OAuth2Userinfo;
import com.ssh.security1.model.User;
import com.ssh.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service @RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //메서드 종료 후 컨트롤러에서 @AuthenticationPrincipal 을 사용해서 유저 정보를 받을 수 있다
    //구글로부터 받은 userRequest 데이터에 대한 후처리 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //registrationId로 어떤 OAuth로 로그인했는지 확인가능
        System.out.println("userRequest.getClientRegistration(): " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken().getTokenValue(): " + userRequest.getAccessToken().getTokenValue());
        
        //userRequest 정보 -> loadUser 메서드 호출 -> 회원프로필
        System.out.println("super.loadUser(userRequest).getAttributes(): " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User.getAttributes(): " + oAuth2User.getAttributes());

        OAuth2Userinfo userinfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {

            System.out.println("구글 로그인 요청");
            userinfo = new GoogleUserinfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {

            System.out.println("네이버 로그인 요청");
            userinfo = new NaverUserinfo((Map<String,Object>) oAuth2User.getAttributes().get("response"));
        }
        String provider = userinfo.getProvider();//userRequest.getClientRegistration().getRegistrationId(); // google
        String providerId = userinfo.getProviderId();//oAuth2User.getAttribute("sub");

        String username = provider+"_"+providerId;  //google_1234567890123456

        //패스워드는 큰의미없음..
        String password = bCryptPasswordEncoder.encode("ASDFASDF");
        String email = userinfo.getEmail();//oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        //회원가입 됐는지 확인
        User userEntity = userRepository.findByUsername(username);

        //회원이 아니면 회원가입을 자동으로 진행
        if(userEntity == null) {
            userEntity = User.builder()
                        .username(username)
                        .password(password)
                        .email(email)
                        .role(role)
                        .provider(provider)
                        .providerId(providerId)
                        .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
