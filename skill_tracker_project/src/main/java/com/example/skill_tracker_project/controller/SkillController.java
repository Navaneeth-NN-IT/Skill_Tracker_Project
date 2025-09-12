package com.example.skill_tracker_project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_tracker_project.dto.SkillDto;
import com.example.skill_tracker_project.service.SkillService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/skills")
@AllArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody SkillDto skillDto) {
        SkillDto savedSkill = skillService.createSkill(skillDto);
        return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable("id") Long skillId) {
        SkillDto skillDto = skillService.getSkillById(skillId);
        return ResponseEntity.ok(skillDto);
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        List<SkillDto> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(@PathVariable("id") Long skillId,
                                                @Valid @RequestBody SkillDto skillDto) {
        SkillDto updatedSkill = skillService.updateSkill(skillId, skillDto);
        return ResponseEntity.ok(updatedSkill);
    }

    @PreAuthorize("hasRole('ADMIN')") // <-- REVERT to hasRole
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable("id") Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok("Skill deleted successfully!");
    }
}