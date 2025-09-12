package com.example.skill_tracker_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_tracker_project.entity.Employee; // <-- Make sure this is imported

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds all employees belonging to a specific department.
     * @param departmentId The ID of the department.
     * @return A list of employees in that department.
     */
    List<Employee> findByDepartment_Id(Long departmentId);

    /**
     * Finds all employees who possess a specific skill.
     * @param skillName The name of the skill to search for.
     * @return A list of employees with the specified skill.
     */
    List<Employee> findByEmployeeSkills_Skill_Name(String skillName);

    /**
     * Finds an employee profile by the associated user's ID.
     * This is crucial for retrieving employee details upon login.
     * @param userId The ID of the user.
     * @return An Optional containing the Employee if found.
     */
    Optional<Employee> findByUser_Id(Long userId); // <-- ADDED METHOD
}