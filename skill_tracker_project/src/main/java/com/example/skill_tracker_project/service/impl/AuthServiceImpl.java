package com.example.skill_tracker_project.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skill_tracker_project.dto.auth.JwtAuthResponse;
import com.example.skill_tracker_project.dto.auth.LoginRequest;
import com.example.skill_tracker_project.dto.auth.RegisterRequest;
import com.example.skill_tracker_project.entity.Employee;
import com.example.skill_tracker_project.entity.User;
import com.example.skill_tracker_project.repository.EmployeeRepository;
import com.example.skill_tracker_project.repository.UserRepository;
import com.example.skill_tracker_project.security.JwtTokenProvider;
import com.example.skill_tracker_project.service.AuthService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after successful login. Data integrity issue."));
        
        Employee employee = employeeRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Employee profile not found for user. Data integrity issue."));
        
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setUserId(user.getId());
        jwtAuthResponse.setEmployeeId(employee.getId());
        jwtAuthResponse.setRole(user.getRole().name());
        jwtAuthResponse.setEmail(user.getEmail()); // <-- ADD THIS LINE

        return jwtAuthResponse;
    }

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());

        User savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(savedUser);
        
        employeeRepository.save(employee);
        
        return "User registered successfully!";
    }
}