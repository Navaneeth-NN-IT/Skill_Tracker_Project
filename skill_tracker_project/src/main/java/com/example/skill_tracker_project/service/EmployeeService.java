
package com.example.skill_tracker_project.service;

import java.util.List;

import com.example.skill_tracker_project.dto.EmployeeDto;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(Long employeeId);
    EmployeeDto updateEmployee(Long employeeId, Long departmentId, Long managerId);
    EmployeeDto assignSkillToEmployee(Long employeeId, Long skillId);
    void removeSkillFromEmployee(Long employeeId, Long skillId);
    List<EmployeeDto> searchEmployeesBySkill(String skillName);
    List<EmployeeDto> searchEmployeesByDepartment(Long departmentId);
}