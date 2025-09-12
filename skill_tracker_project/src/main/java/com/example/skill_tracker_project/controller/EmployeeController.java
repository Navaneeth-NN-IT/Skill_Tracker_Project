package com.example.skill_tracker_project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_tracker_project.dto.AssignSkillRequest;
import com.example.skill_tracker_project.dto.EmployeeDto;
import com.example.skill_tracker_project.service.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestParam(required = false) Long departmentId,
                                                      @RequestParam(required = false) Long managerId) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, departmentId, managerId);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @PostMapping("/{id}/skills")
    public ResponseEntity<EmployeeDto> assignSkill(@PathVariable Long id, @RequestBody AssignSkillRequest request) {
        EmployeeDto updatedEmployee = employeeService.assignSkillToEmployee(id, request.getSkillId());
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @DeleteMapping("/{employeeId}/skills/{skillId}")
    public ResponseEntity<String> removeSkill(@PathVariable Long employeeId, @PathVariable Long skillId) {
        employeeService.removeSkillFromEmployee(employeeId, skillId);
        return ResponseEntity.ok("Skill removed from employee successfully.");
    }
    
    @GetMapping("/search/by-skill")
    public ResponseEntity<List<EmployeeDto>> searchBySkill(@RequestParam String skillName) {
        return ResponseEntity.ok(employeeService.searchEmployeesBySkill(skillName));
    }

    @GetMapping("/search/by-department")
    public ResponseEntity<List<EmployeeDto>> searchByDepartment(@RequestParam Long departmentId) {
        return ResponseEntity.ok(employeeService.searchEmployeesByDepartment(departmentId));
    }
}