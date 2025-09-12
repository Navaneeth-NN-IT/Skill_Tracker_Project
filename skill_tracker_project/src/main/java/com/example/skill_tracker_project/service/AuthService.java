
package com.example.skill_tracker_project.service;

import com.example.skill_tracker_project.dto.auth.JwtAuthResponse;
import com.example.skill_tracker_project.dto.auth.LoginRequest;
import com.example.skill_tracker_project.dto.auth.RegisterRequest;

public interface AuthService {
    /**
     * Authenticates a user and returns a JWT token.
     * @param loginRequest DTO containing email and password.
     * @return A DTO containing the access token.
     */
    JwtAuthResponse login(LoginRequest loginRequest);

    /**
     * Registers a new user in the system.
     * @param registerRequest DTO containing user details for registration.
     * @return A success message.
     */
    String register(RegisterRequest registerRequest);
}