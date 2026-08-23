package com.example.demo.repository;

import com.example.demo.domain.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeRepositoryImpl implements NoticeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Notice notice) {
        em.persist(notice);
    }

    @Override
    public Notice findById(Long id) {
        return em.find(Notice.class, id);
    }

    @Override
    public List<Notice> findByGroupId(Long groupId) {
        return em.createQuery("select n from Notice n where n.studyGroup.id = :groupId", Notice.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
