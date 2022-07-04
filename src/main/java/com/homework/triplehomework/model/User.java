package com.homework.triplehomework.model;

import com.homework.triplehomework.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Table(name = "User_Table")
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
