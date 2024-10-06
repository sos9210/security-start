package com.ssh.security1.config.auth;

import com.ssh.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//시큐리티가 /login 요청을 가로챈다.
// 로그인을 진행이 완료되면 시큐리티 Session을 생성한다. (Security ContextHolder)
// Objecc타입 => Authentication 타입 객체
// Authentacation 안에 User정보가 있어야한다.
@Data
public class PrincipalDetails implements UserDetails {
    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add( () -> user.getRole());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정의 만료 여부 (true : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정의 잠김 여부 (true : 잠기지않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부 (true : 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부 (true : 활성화됨)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
