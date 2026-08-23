package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    public Attendance(User user, Schedule schedule, AttendanceStatus status){
        this.user=user;
        this.schedule=schedule;
        this.status=status;
    }
}
