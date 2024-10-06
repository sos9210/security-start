package com.ssh.security1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter @Setter @Table(name = "USERS")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    //oauth 인증 provider
    private String provider;
    //oauthId (sub값)
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;
}
