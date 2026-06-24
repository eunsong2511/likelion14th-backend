package com.example.demo.repository;

import com.example.demo.domain.StudyGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudyGroupRepositoryImpl implements StudyGroupRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(StudyGroup group) {
        em.persist(group);
    }

    @Override
    public StudyGroup findById(Long id) {
        return em.find(StudyGroup.class, id);
    }

    @Override
    public List<StudyGroup> findAll() {
        return em.createQuery("select g from StudyGroup g", StudyGroup.class)
                .getResultList();
    }
}
