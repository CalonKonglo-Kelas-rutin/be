package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.CreateUserRequestDto;
import com.horolofi.rwa.entity.User;
import com.horolofi.rwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserContoller {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody CreateUserRequestDto request) {
        User createdUser = userService.registerUser(request);
        return ResponseEntity.ok(createdUser);
    }
}