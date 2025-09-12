package com.example.skill_tracker_project.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.skill_tracker_project.dto.DepartmentDto;
import com.example.skill_tracker_project.entity.Department;
import com.example.skill_tracker_project.exception.ResourceNotFoundException;
import com.example.skill_tracker_project.repository.DepartmentRepository;
import com.example.skill_tracker_project.service.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        // Check if a department with the same name already exists to prevent duplicates.
        if (departmentRepository.findByName(departmentDto.getName()).isPresent()) {
            throw new RuntimeException("A department with the name '" + departmentDto.getName() + "' already exists.");
        }
        
        // Create a new Department entity instance without setting the ID.
        Department department = new Department();
        department.setName(departmentDto.getName());
        
        // Save the new entity to the database.
        Department savedDepartment = departmentRepository.save(department);
        
        // Map the saved entity back to a DTO for the response.
        return mapToDto(savedDepartment);
    }

    @Override
    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
        return mapToDto(department);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto updateDepartment(Long departmentId, DepartmentDto updatedDepartmentDto) {
        // Find the existing department by its ID.
        Department departmentToUpdate = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
        
        // Check if the new name is already being used by a DIFFERENT department.
        Optional<Department> existingDeptWithSameName = departmentRepository.findByName(updatedDepartmentDto.getName());
        if (existingDeptWithSameName.isPresent() && !existingDeptWithSameName.get().getId().equals(departmentId)) {
            throw new RuntimeException("Another department with the name '" + updatedDepartmentDto.getName() + "' already exists.");
        }

        // Update the name and save the changes.
        departmentToUpdate.setName(updatedDepartmentDto.getName());
        Department updatedDepartment = departmentRepository.save(departmentToUpdate);
        
        return mapToDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
        departmentRepository.delete(department);
    }

    // Helper method to convert a Department entity to a DepartmentDto.
    private DepartmentDto mapToDto(Department department) {
        return new DepartmentDto(department.getId(), department.getName());
    }
}