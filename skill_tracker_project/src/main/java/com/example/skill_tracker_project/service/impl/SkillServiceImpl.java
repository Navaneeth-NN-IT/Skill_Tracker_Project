package com.example.skill_tracker_project.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.skill_tracker_project.dto.SkillDto;
import com.example.skill_tracker_project.entity.Skill;
import com.example.skill_tracker_project.exception.ResourceNotFoundException;
import com.example.skill_tracker_project.repository.SkillRepository;
import com.example.skill_tracker_project.service.SkillService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public SkillDto createSkill(SkillDto skillDto) {
        // Check if a skill with the same name already exists to prevent duplicates.
        if (skillRepository.findByName(skillDto.getName()).isPresent()) {
            throw new RuntimeException("A skill with the name '" + skillDto.getName() + "' already exists.");
        }
        
        // Create a new Skill entity instance.
        // We do NOT set the ID, as we want the database to generate it automatically.
        Skill skill = new Skill();
        skill.setName(skillDto.getName());
        
        // Save the new entity to the database.
        Skill savedSkill = skillRepository.save(skill);
        
        // Map the saved entity (which now has an ID) back to a DTO to return to the client.
        return mapToDto(savedSkill);
    }

    @Override
    public SkillDto getSkillById(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));
        return mapToDto(skill);
    }

    @Override
    public List<SkillDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SkillDto updateSkill(Long skillId, SkillDto updatedSkillDto) {
        // Find the existing skill by its ID.
        Skill skillToUpdate = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));

        // Check if the new name is already being used by a DIFFERENT skill.
        Optional<Skill> existingSkillWithSameName = skillRepository.findByName(updatedSkillDto.getName());
        if (existingSkillWithSameName.isPresent() && !existingSkillWithSameName.get().getId().equals(skillId)) {
            throw new RuntimeException("Another skill with the name '" + updatedSkillDto.getName() + "' already exists.");
        }

        // Update the name and save the changes.
        skillToUpdate.setName(updatedSkillDto.getName());
        Skill updatedSkill = skillRepository.save(skillToUpdate);
        
        return mapToDto(updatedSkill);
    }

    @Override
    public void deleteSkill(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));
        skillRepository.delete(skill);
    }

    // Helper method to convert a Skill entity to a SkillDto.
    private SkillDto mapToDto(Skill skill) {
        return new SkillDto(skill.getId(), skill.getName());
    }
}