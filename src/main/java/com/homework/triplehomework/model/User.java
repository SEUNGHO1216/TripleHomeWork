package com.homework.triplehomework.model;

import com.homework.triplehomework.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Table(name = "User_Table", indexes = {@Index(name = "user_index", columnList = "userId")})
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;
    @Column(unique = true)
    private String username;
    private String password;

    public User(String username, String password){
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
    }
}
