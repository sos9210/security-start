package com.ssh.security1.controller;

import com.ssh.security1.model.User;
import com.ssh.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @GetMapping({"","/"})
    public String index() {
        //머스테치는 기본 폴더 resources..
        return "index";
    }

    @GetMapping("/user")
    public String user() {
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
