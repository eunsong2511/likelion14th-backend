package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long signUp(Member member){
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        member.setPassword(hashedPassword);

        memberRepository.save(member);
        return member.getId();
    }


    public String login(String userId, String password){
        Member member = memberRepository.findByUserId(userId);
        if(member!=null&&BCrypt.checkpw(password, member.getPassword())){
            String token = jwtUtil.generateJwt(member.getUserId(), member.getUsername());
            return token;
        }
        return "아이디와 비밀번호를 확인하세요";
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }


    public Member tokenToMember(String token){
        return memberRepository.findByUserId(jwtUtil.getClaimsFromJwt(token).getSubject());
    }


    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }


    public void update(Long id, String newName, String newPassword){
        Member member = memberRepository.findById(id);

        member.setUsername(newName);


        if (newPassword!=null&&!newPassword.isEmpty()){
            String hashedPassword=BCrypt.hashpw(newPassword, BCrypt.gensalt());
            member.setPassword(hashedPassword);
        }

        memberRepository.save(member);

    }

    @Transactional
    public void delete(Long id){
        memberRepository.remove(id);
    }

    public Member findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }
}
