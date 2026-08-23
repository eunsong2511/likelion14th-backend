package com.example.demo.repository;

import com.example.demo.domain.Notice;

import java.util.List;

public interface NoticeRepository {
    void save(Notice notice);
    Notice findById(Long id);
    List<Notice> findByGroupId(Long groupId);
}
