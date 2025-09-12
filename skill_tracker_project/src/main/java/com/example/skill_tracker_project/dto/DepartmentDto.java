
package com.example.skill_tracker_project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private Long id;

    @NotEmpty(message = "Department name cannot be empty")
    private String name;
}