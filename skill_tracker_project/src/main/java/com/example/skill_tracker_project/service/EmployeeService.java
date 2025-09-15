package com.example.skill_tracker_project.service;

import com.example.skill_tracker_project.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    /**
     * Retrieves a paginated and sorted list of all employees.
     *
     * @param pageable an object containing pagination (page, size) and sorting (sort by, direction) information.
     * @return a Page object containing the list of EmployeeDtos for the requested page and pagination metadata.
     */
    Page<EmployeeDto> getAllEmployees(Pageable pageable);

    /**
     * Retrieves a single employee by their unique ID.
     *
     * @param employeeId the ID of the employee to retrieve.
     * @return the EmployeeDto for the found employee.
     */
    EmployeeDto getEmployeeById(Long employeeId);

    /**
     * Updates an employee's department and/or manager.
     *
     * @param employeeId   the ID of the employee to update.
     * @param departmentId the new department ID (can be null).
     * @param managerId    the new manager's user ID (can be null).
     * @return the updated EmployeeDto.
     */
    EmployeeDto updateEmployee(Long employeeId, Long departmentId, Long managerId);

    /**
     * Assigns a specific skill to an employee.
     *
     * @param employeeId the ID of the employee.
     * @param skillId    the ID of the skill to assign.
     * @return the updated EmployeeDto with the new skill.
     */
    EmployeeDto assignSkillToEmployee(Long employeeId, Long skillId);

    /**
     * Removes a specific skill from an employee.
     *
     * @param employeeId the ID of the employee.
     * @param skillId    the ID of the skill to remove.
     */
    void removeSkillFromEmployee(Long employeeId, Long skillId);

    /**
     * Searches for all employees who possess a specific skill.
     *
     * @param skillName the name of the skill to search for.
     * @return a List of EmployeeDtos who have the skill.
     */
    List<EmployeeDto> searchEmployeesBySkill(String skillName);

    /**
     * Searches for all employees within a specific department.
     *
     * @param departmentId the ID of the department.
     * @return a List of EmployeeDtos in the department.
     */
    List<EmployeeDto> searchEmployeesByDepartment(Long departmentId);
}