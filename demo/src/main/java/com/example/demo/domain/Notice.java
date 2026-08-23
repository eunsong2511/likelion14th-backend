package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Notice {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    private StudyGroup studyGroup;

    public Notice(String title, String content, StudyGroup studyGroup){
        this.title=title;
        this.content=content;
        this.studyGroup = studyGroup;
    }
}
