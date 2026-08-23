package com.example.demo.repository;

import com.example.demo.domain.Schedule;

import java.util.List;

public interface ScheduleRepository {
    void save(Schedule schedule);
    Schedule findById(Long id);
    List<Schedule> findByGroupId(Long groupId);
}
