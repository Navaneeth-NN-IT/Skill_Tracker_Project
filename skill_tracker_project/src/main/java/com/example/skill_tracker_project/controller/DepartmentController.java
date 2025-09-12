package com.example.skill_tracker_project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_tracker_project.dto.DepartmentDto;
import com.example.skill_tracker_project.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto savedDepartment = departmentService.createDepartment(departmentDto);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long departmentId) {
        DepartmentDto departmentDto = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(departmentDto);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long departmentId,
                                                          @Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
        return ResponseEntity.ok(updatedDepartment);
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Department deleted successfully!");
    }
}