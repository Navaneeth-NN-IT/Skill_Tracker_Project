
package com.example.skill_tracker_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_tracker_project.dto.auth.JwtAuthResponse;
import com.example.skill_tracker_project.dto.auth.LoginRequest;
import com.example.skill_tracker_project.dto.auth.RegisterRequest;
import com.example.skill_tracker_project.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

   
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}