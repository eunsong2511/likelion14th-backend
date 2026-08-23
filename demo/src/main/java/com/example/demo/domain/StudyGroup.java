package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class StudyGroup {

    @Id
    @GeneratedValue
    private Long id;

    private String groupName;
    private String description;

    public StudyGroup(String groupName, String description){
        this.groupName=groupName;
        this.description=description;
    }
}
