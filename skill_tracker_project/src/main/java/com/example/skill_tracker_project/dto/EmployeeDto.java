package com.example.skill_tracker_project.dto;

import java.util.List;

import com.example.skill_tracker_project.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // <-- IMPORT

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private DepartmentDto department;
    private ManagerDto manager;
    private List<SkillDto> skills; // <-- CHANGE TO LIST

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManagerDto {
        private Long id;
        private String name;
    }
}