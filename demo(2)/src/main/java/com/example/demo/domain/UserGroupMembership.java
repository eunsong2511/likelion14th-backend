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
public class UserGroupMembership {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyGroup studyGroup;

    @ManyToOne
    private User user;

    private LocalDate joinedTime;

    public UserGroupMembership(StudyGroup studyGroup, User user, LocalDate joinedTime){
        this.studyGroup=studyGroup;
        this.user=user;
        this.joinedTime=joinedTime;
    }
}
