package com.example.demo.repository;

import com.example.demo.domain.StudyGroup;

import java.util.List;

public interface StudyGroupRepository {
    void save(StudyGroup group);
    StudyGroup findById(Long id);
    List<StudyGroup> findAll();
}
