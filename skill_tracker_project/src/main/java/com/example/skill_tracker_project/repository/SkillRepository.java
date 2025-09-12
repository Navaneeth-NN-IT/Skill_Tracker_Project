
package com.example.skill_tracker_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_tracker_project.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    /**
     * Finds a skill by its unique name.
     * @param name The name of the skill.
     * @return An optional containing the Skill if found.
     */
    Optional<Skill> findByName(String name);
}