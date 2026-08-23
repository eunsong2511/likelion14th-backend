package com.example.demo.controller;
//Http 요청 받고 전달

import com.example.demo.DTO.MemberDTO;
import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController //Controller 빈 등록, 반환값 JSON 자동 변환
@RequiredArgsConstructor //final 필드 사용하는 생성자 자동 생성 -> 의존성 주입
@RequestMapping("/api") //Http 요청과 클래스 연결, url에 /api가 붙음 -> 컨트롤러로 요청 들어옴
public class MemberController {
    private final MemberService memberService; //final -> @requried~ 어노테이션으로 생성자 생성시 매개변수로 들어감
    private final JwtUtil jwtUtil;              //클래스 빈과 필드 빈 연결됨 - 의존성 생김

    //회원 가입 요청
    @PostMapping("/members") //post 요청 처리 시 url에 /members 붙음 -> /api/members
    public MemberDTO.Result<Long> saveMember (@RequestBody MemberDTO.Request.Create request){
        //data 반환타입 Long인 메서드, 매개변수 DTO 받음
        //@RequestBody : JSON->JAVA 변환
        Member member = new Member(); //Domain 객체 생성
        member.setUserId(request.getUserId()); //DTO의 값 -> Domain Member에 전달
        member.setPassword(request.getPassword());
        member.setUsername(request.getUsername());

        Long id = memberService.signUp(member);
        //member 객체로 memberService에서 signUp 로직 처리
        //memberService.signUp으로 반환받은 값 Long id에 저장
        //MemberDTO.Result<> 타입으로 id 반환
        return new MemberDTO.Result<>(id);
    }

    //로그인 요청
    @PostMapping("/login")
    public MemberDTO.Result<String> login(@RequestBody MemberDTO.Request.Login request){
        String token = memberService.login(request.getUserId(), request.getPassword());
        //매개변수 request 객체에서 id, password 가져옴
        //Service에서 login 로직 처리 후 반환받은 값(jwt 토큰) token에 저장
        return new MemberDTO.Result<>(token); //token 반환
    }

    //전체 회원 조회 요청
    @GetMapping("/members")
    public MemberDTO.Result<List<MemberDTO.Response.Member>> findAllMembers(){ //회원 DTO 리스트 타입 반환하는 메서드

        List<Member> findMembers = memberService.findAll(); //Service에서 findAll 로직 실행 -> 멤버타입 리스트 findMembers에 저장

        List<MemberDTO.Response.Member> collect = findMembers.stream().map(m->new MemberDTO.Response.Member(m.getId(), m.getUserId(), m.getUsername())).collect(Collectors.toList());
        //findMembers(Service에서 받은 값) -> stream : 리스트 반복 처리 기능 -> map : 데이터 형태 변환
        //Domain 멤버 객체 -> 리스트<DTO 멤버 객체> 저장

        return new MemberDTO.Result<>(collect); //collect 리스트 반환
    }

    //회원 정보 수정 요청
    @PutMapping("/members")
    public MemberDTO.Result<?> updateMember(@RequestBody MemberDTO.Request.Update request, @RequestHeader("Authorization") String token){
                                            //Body JSON 데이터 객체로 변환, Header Authorization 추출 -> token에 저장
        if(!jwtUtil.validateJwt(token)){    //토큰 유효 검사 - 검증 실패 시
            return new MemberDTO.Result<>("유효한 토큰이 아닙니다.");
        }

        Long id = memberService.tokenToMember(token).getId(); //로직1. Service tokenToMember 메서드 - 토큰으로 회원조회 후 id 가져오기
        memberService.update(id, request.getUsername(), request.getPassword()); //로직2. Service update 메서드 - 실질적 회원정보 수정 로직

        Member findmember = memberService.findById(id); //로직3. Service findById 메서드 - id로 회원조회 후 값 가져오기
        return new MemberDTO.Result<>(new MemberDTO.Response.Member(findmember.getId(), findmember.getUserId(), findmember.getUsername()));
        //수정된 회원정보 반환
    }

    //회원 삭제 요청
    @DeleteMapping("/members")
    public MemberDTO.Result<String> deleteMember(@RequestHeader("Authorization") String token){

        //토큰값 유효X 경우
        if(!jwtUtil.validateJwt(token)){
            return new MemberDTO.Result<>("유효한 토큰이 아닙니다.");
        }

        //Service.tokenToMember : 토큰으로 회원조회 로직 - id 가져오기
        Long id = memberService.tokenToMember(token).getId();

        //Service 회원 삭제 로직 실행
        memberService.delete(id);

        return new MemberDTO.Result<>("회원삭제 완료");
    }

    @pathvariable
}

//Controller : 요청 처리 위해 Service의 로직 실행 -> 응답 반환

//Service
    //로직 : 회원가입 / 로그인 / 회원조회(전체/개별)(id/token 이용) / 회원정보수정 / 회원정보삭제

    //1. DB에 필요 처리 명령 = Ropository 메서드 실행
    //2. BCrypt 비밀번호 해싱, 확인
    //3. Jwt 토큰 생성

    //Domain Member클래스 객체 활용
        //Repository에서 값 가져와서 member객체에 저장
            //set 새로운값
            //return get 필요값
        //member객체를 Repository에 전달
            //Repository.메서드(매개변수member)로 전달

//Repository : DB에서 회원정보 저장 / 조회 / 삭제



//ex 회원정보수정 요청

//Controller : 토큰값 확인

    //Service : 회원조회(token) 로직, 회원 id 가져오기(반환)
        //Repository : DB에서 회원정보 조회(반환)

    //Service : 회원정보수정 로직
        //Repository : DB에서 회원정보 조회(반환)
        //Repository : DB에 회원정보 저장

    //Service : 회원조회(id) 로직, 회원 정보 가져오기(반환)
        //Repository : id로 DB에서 회원정보 조회(반환)

//Controller : 수정된 회원정보 반환



