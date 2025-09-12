package com.example.skill_tracker_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.skill_tracker_project.entity.User;
import com.example.skill_tracker_project.repository.UserRepository;

@SpringBootApplication
public class SkillTrackerProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillTrackerProjectApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner resetAdminPassword(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Find the admin user by their email
            User admin = userRepository.findByEmail("admin@company.com").orElse(null);

            if (admin != null) {
                // If the user exists, encode the new password and save the user
                String encodedPassword = passwordEncoder.encode("password123");
                admin.setPassword(encodedPassword); // The 'setPassword' method comes from the UserDetails implementation
                userRepository.save(admin);
                System.out.println(">>>>>>>>>> Admin password has been successfully reset! <<<<<<<<<<");
            } else {
                // If the user is not found in the database
                System.out.println(">>>>>>>>>> Admin user (admin@company.com) not found. No password was reset. <<<<<<<<<<");
            }
        };
    } 
}