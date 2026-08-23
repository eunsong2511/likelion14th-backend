package com.example.demo.repository;
//인터페이스 -> MemberRepositoryImpl 구현
import com.example.demo.domain.Member;

import java.util.List;

//어떤 기능이 들어갈지 정의
public interface MemberRepository {
    void save(Member member);
    Member findById(Long id);
    List<Member> findAll();
    void remove(Long id);
    Member findByUserId(String userId);
}
