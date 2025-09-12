
package com.example.skill_tracker_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_tracker_project.entity.EmployeeSkill;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {

    /**
     * Finds the specific EmployeeSkill link between an employee and a skill.
     * This is useful for deleting a specific skill assignment.
     *
     * @param employeeId The ID of the employee.
     * @param skillId The ID of the skill.
     * @return An optional containing the EmployeeSkill entity if the link exists.
     */
    Optional<EmployeeSkill> findByEmployee_IdAndSkill_Id(Long employeeId, Long skillId);
}