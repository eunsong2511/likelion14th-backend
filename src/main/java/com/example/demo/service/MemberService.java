package com.example.demo.service;
//실제 로직 처리
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;


@Service //Service 빈 등록 어노테이션
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    //회원가입 로직 (비밀번호 암호화 처리, Repository 메서드 실행(DB저장), id 반환)
    public Long signUp(Member member){
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        member.setPassword(hashedPassword);
        //비밀번호 암호화 처리 : BCrypt 이용해서 해싱 -> hashedPassword에 저장 -> member.setPassword
        memberRepository.save(member); //Repository로 이동
        return member.getId(); //id값 가져와서 반환
    }

    //로그인 로직 (회원 조회 -> BCrypt 비밀번호 검증 -> jwt 토큰 반환)
    public String login(String userId, String password){
        Member member = memberRepository.findByUserId(userId); //DB에서 회원 조회 : Repository에서 처리 -> member로 가져옴
        if(member!=null&&BCrypt.checkpw(password, member.getPassword())){ //회원 존재 && 입력값, 암호화된 비밀번호 비교
            String token = jwtUtil.generateJwt(member.getUserId(), member.getUsername()); //jwt 토큰 생성
            return token; //jwt 토큰 반환
        }
        return "아이디와 비밀번호를 확인하세요";
    }

    //전체 회원 조회 로직 (Repository에서 조회한 값 반환)
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //토큰으로 회원 조회 로직
    public Member tokenToMember(String token){
        return memberRepository.findByUserId(jwtUtil.getClaimsFromJwt(token).getSubject());
    }

    //id로 회원 조회 로직
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //회원 정보 수정 로직 (id로 회원조회 -> 새 정보 member에 저장 -> member값 Repository에 저장)
    public void update(Long id, String newName, String newPassword){
        Member member = memberRepository.findById(id);
        //수정할 기존 회원 정보 가져오기 - Repository : id로 회원 조회 후 반환

        //새 이름 저장
        member.setUsername(newName);

        //새 비밀번호 저장
        if (newPassword!=null&&!newPassword.isEmpty()){ //비밀번호 수정 조건
            //비밀번호 입력한 경우(True) newPassword~(빈 문자열 체크) 실행
            String hashedPassword=BCrypt.hashpw(newPassword, BCrypt.gensalt());
            member.setPassword(hashedPassword);
            //비밀번호 해싱 후 저장
        }

        memberRepository.save(member);
        //Repository : DB에 새로운 회원 정보(member) 저장
    }

    //회원 삭제 로직
    public void delete(Long id){
        memberRepository.remove(id);
    }

    public Member findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }


}
