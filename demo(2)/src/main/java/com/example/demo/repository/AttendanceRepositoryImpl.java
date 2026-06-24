package com.example.demo.repository;

import com.example.demo.domain.Attendance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttendanceRepositoryImpl implements AttendanceRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Attendance attendance) {
        em.persist(attendance);
    }

    @Override
    public Attendance findById(Long id) {
        return em.find(Attendance.class, id);
    }

    @Override
    public List<Attendance> findByScheduleId(Long scheduleId) {
        return em.createQuery("select a from Attendance a where a.schedule.id = :scheduleId", Attendance.class)
                .setParameter("scheduleId", scheduleId)
                .getResultList();
    }

    @Override
    public List<Attendance> findByUserId(Long userId) {
        return em.createQuery("select a from Attendance a where a.user.id = :userId", Attendance.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
