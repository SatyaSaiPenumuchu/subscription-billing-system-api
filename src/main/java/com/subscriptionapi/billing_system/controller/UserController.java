package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.dto.UserRegistrationDTO;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully.");
    }
}

