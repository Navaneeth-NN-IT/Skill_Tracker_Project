package com.example.skill_tracker_project.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skill_tracker_project.dto.DepartmentDto;
import com.example.skill_tracker_project.dto.EmployeeDto;
import com.example.skill_tracker_project.dto.SkillDto;
import com.example.skill_tracker_project.entity.Department;
import com.example.skill_tracker_project.entity.Employee;
import com.example.skill_tracker_project.entity.EmployeeSkill;
import com.example.skill_tracker_project.entity.Skill;
import com.example.skill_tracker_project.entity.User;
import com.example.skill_tracker_project.exception.ResourceNotFoundException;
import com.example.skill_tracker_project.repository.DepartmentRepository;
import com.example.skill_tracker_project.repository.EmployeeRepository;
import com.example.skill_tracker_project.repository.EmployeeSkillRepository;
import com.example.skill_tracker_project.repository.SkillRepository;
import com.example.skill_tracker_project.repository.UserRepository;
import com.example.skill_tracker_project.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, UserRepository userRepository, SkillRepository skillRepository, EmployeeSkillRepository employeeSkillRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.employeeSkillRepository = employeeSkillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(this::mapToDto); // Use the built-in map function of the Page object
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        return mapToDto(employee);
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, Long departmentId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        if (managerId != null) {
            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Manager (User)", "id", managerId));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDto assignSkillToEmployee(Long employeeId, Long skillId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));

        if (employeeSkillRepository.findByEmployee_IdAndSkill_Id(employeeId, skillId).isPresent()) {
             throw new RuntimeException("Employee already has this skill.");
        }

        EmployeeSkill employeeSkill = new EmployeeSkill();
        employeeSkill.setEmployee(employee);
        employeeSkill.setSkill(skill);
        employee.getEmployeeSkills().add(employeeSkill);
        
        employeeRepository.save(employee);

        return mapToDto(employee);
    }

    @Override
    @Transactional
    public void removeSkillFromEmployee(Long employeeId, Long skillId) {
        EmployeeSkill employeeSkill = employeeSkillRepository.findByEmployee_IdAndSkill_Id(employeeId, skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill assignment not found for employee " + employeeId + " and skill " + skillId));
        employeeSkillRepository.delete(employeeSkill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> searchEmployeesBySkill(String skillName) {
        return employeeRepository.findByEmployeeSkills_Skill_Name(skillName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> searchEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartment_Id(departmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());

        User user = employee.getUser();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        if (employee.getDepartment() != null) {
            dto.setDepartment(new DepartmentDto(employee.getDepartment().getId(), employee.getDepartment().getName()));
        }

        if (employee.getManager() != null) {
            dto.setManager(new EmployeeDto.ManagerDto(employee.getManager().getId(), employee.getManager().getName()));
        }

        List<SkillDto> skills = employee.getEmployeeSkills().stream()
                .map(employeeSkill -> new SkillDto(employeeSkill.getSkill().getId(), employeeSkill.getSkill().getName()))
                .collect(Collectors.toList());
        dto.setSkills(skills);

        return dto;
    }
}