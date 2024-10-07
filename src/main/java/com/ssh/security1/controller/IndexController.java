package com.ssh.security1.controller;

import com.ssh.security1.config.auth.PrincipalDetails;
import com.ssh.security1.model.User;
import com.ssh.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @ResponseBody
    @GetMapping("/test/login")      //2가지 방법으로 로그인 유저정보를 받아온다..
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("=================== /test/login =========================");
        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getUsername() = " + principalDetails.getUsername());

        System.out.println("userDetails.getUsername() = " + userDetails.getUser());
        return "세션정보 확인하기";
        
    }
    @ResponseBody
    @GetMapping("/test/oauth/login") //2가지 방법으로 OAuth 유저정보를 받아온다..
    public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("=================== /test/oauth/login =========================");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oauth2User.getAttributes() = " + oauth2User.getAttributes());

        System.out.println("oauth.getAttributes()" + oauth.getAttributes());
        return "oauth 세션정보 확인하기";

    }
        
    @GetMapping({"","/"})
    public String index() {
        //머스테치는 기본 폴더 resources..
        return "index";
    }


    //일반로그인 , OAuth로그인 사용자 모두 PrincipalDetails 타입으로 받을 수 있다.
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("principalDetails : " + userDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String joinProc(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    //특정 URL에 접근제한하고 싶다하면 사용!
    @Secured("ROLE_ADMIN") //해당 권한있는 계정만 접근가능
    @ResponseBody
    @GetMapping("/info")
    public String info() {
        return "개인정보";
    }

    //특정 URL에 접근제한하고 싶다하면 사용!

    //PostAuthorize() // data() 실행후 수행
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //해당 권한있는 계정만 접근가능 (data() 실행전에 수행)
    @ResponseBody
    @GetMapping("/data")
    public String data() {
        return "개인정보";
    }
}
