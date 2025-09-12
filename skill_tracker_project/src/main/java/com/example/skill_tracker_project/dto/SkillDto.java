
package com.example.skill_tracker_project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
    private Long id;

    @NotEmpty(message = "Skill name cannot be empty")
    private String name;
}