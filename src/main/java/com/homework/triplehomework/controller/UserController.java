package com.homework.triplehomework.controller;

import com.homework.triplehomework.dto.UserDto;
import com.homework.triplehomework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDto userDto){
        return userService.signUp(userDto);
    }
}
