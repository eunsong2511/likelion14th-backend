package com.example.demo.repository;

import com.example.demo.domain.UserGroupMembership;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserGroupMembershipRepositoryImpl implements UserGroupMembershipRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(UserGroupMembership membership) {
        em.persist(membership);
    }

    @Override
    public UserGroupMembership findById(Long id) {
        return em.find(UserGroupMembership.class, id);
    }

    @Override
    public List<UserGroupMembership> findByUserId(Long userId) {
        return em.createQuery("select m from UserGroupMembership m where m.user.id = :userId", UserGroupMembership.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<UserGroupMembership> findByGroupId(Long groupId) {
        return em.createQuery("select m from UserGroupMembership m where m.studyGroup.id = :groupId", UserGroupMembership.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
