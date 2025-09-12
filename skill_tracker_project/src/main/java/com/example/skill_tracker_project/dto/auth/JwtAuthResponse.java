
package com.example.skill_tracker_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
      private Long userId;      // <-- ADD
    private Long employeeId;  // <-- ADD
    private String role;      // <-- ADD
     private String email; // <-- ADD THIS FIELD
}