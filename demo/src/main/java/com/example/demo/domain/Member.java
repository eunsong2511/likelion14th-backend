package com.example.demo.domain;

import jakarta.persistence.Column;
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
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String userId;
    private String password;
    private String username;


    public Member(String userId, String password, String username){
        this.userId=userId;
        this.password=password;
        this.username=username;
    }
}
