package com.example.demo.repository;

import com.example.demo.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    User findById(Long id);
    List<User> findAll();
    User findByEmail(String email);
}
