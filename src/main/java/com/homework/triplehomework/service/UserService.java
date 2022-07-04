package com.homework.triplehomework.service;

import com.homework.triplehomework.dto.UserDto;
import com.homework.triplehomework.model.User;
import com.homework.triplehomework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //회원가입 서비스
    public ResponseEntity<String> signUp(UserDto userDto) {

        String username=userDto.getUsername();
        String password=userDto.getPassword();
        password=passwordEncoder.encode(password);
        duplicatedCheck(username);
        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.status(200).body("200");
    }

    //중복체크
    public void duplicatedCheck(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) {
            throw new IllegalArgumentException("중복된 유저네임입니다.");
        }
    }
}
