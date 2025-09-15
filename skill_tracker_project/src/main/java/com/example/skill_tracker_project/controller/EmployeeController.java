package com.example.skill_tracker_project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Using an explicit constructor for dependency injection is a best practice.
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a paginated and sorted list of all employees.
     * This endpoint manually processes the sorting parameters to allow sorting on nested properties
     * of the related User entity (e.g., 'name', 'email').
     *
     * @param pageable Automatically constructed by Spring from URL parameters like ?page=0&size=5&sort=name,asc.
     *                 The @PageableDefault sets a safe default sorting order.
     * @return A ResponseEntity containing a Page of EmployeeDtos.
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(
            @PageableDefault(sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        
        // --- FIX FOR NESTED SORTING ---
        // This block translates simple sort fields like 'name' into the correct JPA path 'user.name'.
        List<Sort.Order> processedOrders = pageable.getSort().stream()
            .map(order -> {
                if ("name".equals(order.getProperty())) {
                    return new Sort.Order(order.getDirection(), "user.name");
                }
                if ("email".equals(order.getProperty())) {
                    return new Sort.Order(order.getDirection(), "user.email");
                }
                // For other fields like 'department.name', JPA can handle it directly.
                return order;
            })
            .collect(Collectors.toList());

        // Create a new Pageable object with the corrected sort information.
        Pageable processedPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by(processedOrders)
        );

        return ResponseEntity.ok(employeeService.getAllEmployees(processedPageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestParam(required = false) Long departmentId,
                                                      @RequestParam(required = false) Long managerId) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, departmentId, managerId);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/skills")
    public ResponseEntity<EmployeeDto> assignSkill(@PathVariable Long id, @RequestBody AssignSkillRequest request) {
        EmployeeDto updatedEmployee = employeeService.assignSkillToEmployee(id, request.getSkillId());
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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