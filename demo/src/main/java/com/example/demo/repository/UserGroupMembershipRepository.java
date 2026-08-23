package com.example.demo.repository;

import com.example.demo.domain.UserGroupMembership;

import java.util.List;

public interface UserGroupMembershipRepository {
    void save(UserGroupMembership membership);
    UserGroupMembership findById(Long id);
    List<UserGroupMembership> findByUserId(Long userId);
    List<UserGroupMembership> findByGroupId(Long groupId);
}