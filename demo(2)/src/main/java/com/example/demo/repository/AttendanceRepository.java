package com.example.demo.repository;

import com.example.demo.domain.Attendance;

import java.util.List;

public interface AttendanceRepository {
    void save(Attendance attendance);
    Attendance findById(Long id);
    List<Attendance> findByScheduleId(Long scheduleId);
    List<Attendance> findByUserId(Long userId);
}
