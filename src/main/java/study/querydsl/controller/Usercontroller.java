package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.User;
import study.querydsl.repository.UserRepository;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Usercontroller {

    private final UserRepository userRepository;

    @PostMapping("/user/login")
    public ResponseEntity<String> receiveData(@RequestBody Map<String, Object> payload) {
        System.out.println("받은 데이터: " + payload);
        return ResponseEntity.ok("Spring Boot가 데이터를 받았습니다!");
    }

}
