
package com.example.skill_tracker_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_tracker_project.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    /**
     * Finds a department by its unique name.
     * @param name The name of the department.
     * @return An optional containing the Department if found.
     */
    Optional<Department> findByName(String name);
}