package com.example.demo.repository;

import com.example.demo.domain.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Schedule schedule) {
        em.persist(schedule);
    }

    @Override
    public Schedule findById(Long id) {
        return em.find(Schedule.class, id);
    }

    @Override
    public List<Schedule> findByGroupId(Long groupId) {
        return em.createQuery("select s from Schedule s where s.studyGroup.id = :groupId", Schedule.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}


