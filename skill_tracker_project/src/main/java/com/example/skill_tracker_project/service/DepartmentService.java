
package com.example.skill_tracker_project.service;

import java.util.List;

import com.example.skill_tracker_project.dto.DepartmentDto;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto getDepartmentById(Long departmentId);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto updateDepartment(Long departmentId, DepartmentDto updatedDepartment);
    void deleteDepartment(Long departmentId);
}