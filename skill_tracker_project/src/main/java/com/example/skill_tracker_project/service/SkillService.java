
package com.example.skill_tracker_project.service;

import java.util.List;

import com.example.skill_tracker_project.dto.SkillDto;

public interface SkillService {
    SkillDto createSkill(SkillDto skillDto);
    SkillDto getSkillById(Long skillId);
    List<SkillDto> getAllSkills();
    SkillDto updateSkill(Long skillId, SkillDto updatedSkill);
    void deleteSkill(Long skillId);
}