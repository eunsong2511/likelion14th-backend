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
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private LocalDate date;

    @ManyToOne
    private StudyGroup studyGroup;

    public Schedule(String title, LocalDate date, StudyGroup studyGroup){
        this.title=title;
        this.date=date;
        this.studyGroup = studyGroup;
    }
}
