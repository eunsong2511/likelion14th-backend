package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor //기본생성자 자동생성 어노테이션
public class Member {
    //필드
    private Long id;
    private String userId;
    private String password;
    private String username;

    //기본생성자
    //빈 객체 생성 후 채워넣기 위함 ex) setter, Jackson, DB변환과정

    //매개변수 생성자
    public Member(String userId, String password, String username){
        this.userId=userId;
        this.password=password;
        this.username=username;
    }
}
