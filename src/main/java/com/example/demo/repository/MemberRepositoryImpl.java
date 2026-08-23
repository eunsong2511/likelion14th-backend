package com.example.demo.repository;
//인터페이스 실제 구현 클래스
//인터페이스의 메서드 -> @Override 해서 사용
import com.example.demo.domain.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j // 콘솔에 상태를 찍기 위해 사용 (Lombok)
@Repository // Repository 빈 등록
public class MemberRepositoryImpl implements MemberRepository {

    //DB 대신 JSON파일 사용하기 위해 쓴 필드
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DATA_FILE_PATH = "data/members.json";
    private final Map<Long, Member> store = new ConcurrentHashMap<>();
    //ID 자동 증가 시퀀스
    private final AtomicLong sequence = new AtomicLong(0L);

    //앱 시작 시 파일 → 메모리 로드 (의존성 주입)
    public MemberRepositoryImpl() { //생성자
        loadDataFromFile();
    }
    private void loadDataFromFile() { //메서드
        File file = new File(DATA_FILE_PATH);
        if (file.exists()) {
            try {
                List<Member> members = objectMapper.readValue(file, new TypeReference<List<Member>>() {
                });
                for (Member member : members) {
                    store.put(member.getId(), member);
                    if (member.getId() > sequence.get()) {
                        sequence.set(member.getId()+1);
                    }
                }
                log.info("회원 데이터 로드 완료: {}명", members.size());
            } catch (IOException e) {
                log.error("회원 데이터 로드 실패", e);
            }
        } else {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            log.info("기존 데이터 파일이 없어 새로 시작합니다.");
        }
    }
    //여기까지 회원데이터 로드 로직

    //메모리 내용을 JSON 파일에 저장 (DB대체)
    private void saveDataToFile() {
        try {
            List<Member> members = new ArrayList<>(store.values());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), members);
        } catch (IOException e) {
            log.error("회원 데이터 저장 실패", e);
            throw new RuntimeException("데이터 저장 실패", e);
        }
    }
    //여기까지 회원데이터 저장 로직

    //회원가입 : DB에 데이터 저장
    @Override
    public void save(Member member) {
        if (member.getId() == null) { //신규 회원 판단

            //id 부여, domain member 객체에 id 저장
            long newId = sequence.incrementAndGet();
            member.setId(newId);

            //DB(map)에 저장
            store.put(newId, member);

        } else { //회원정보 존재시 ex) 기존회원의 정보수정
            store.put(member.getId(), member); //id 가져오기
        }
        saveDataToFile(); //JSON 파일로 저장
    }

    //DB에서 회원 조회 후 회원데이터 반환
    @Override
    public Member findByUserId(String userId) {
        return store.values().stream()
                .filter(member -> member.getUserId().equals(userId))
                .findAny()
                .orElse(null);
    }

    //DB에서 전체 회원 조회 후 회원데이터 리스트형태로 반환
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    //DB에서 id로 회원 조회 후 데이터 반환
    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    //DB에서 회원 정보 삭제
    @Override
    public void remove(Long id) {
        store.remove(id);
        saveDataToFile();
    }


}


//    // JSON 파일 읽기/쓰기용 Jackson 객체
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    // 회원 데이터를 저장할 파일 위치
//    private final String DATA_FILE_PATH = "data/members.json";
//    // 메모리 저장소 (실무에서는 DB가 대신함)
//    private final Map<Long, Member> store = new ConcurrentHashMap<>();
//    // ID 자동 증가 시퀀스
//    private final AtomicLong sequence = new AtomicLong(0L);
//    /**
//     * 회원을 저장하는 메서드입니다.
//     *
//     * 사용 시나리오:
//     * - 새로운 회원 가입 (id가 없음)
//     * - 기존 회원 정보 수정 (id가 이미 있음)
//     *
//     * [동작 설명]
//     * 1. member.getId()가 null이면 → "처음 저장하는 새 회원"이라는 뜻!
//     * → 회원에게 고유번호(ID)를 새로 만들어서 달아준다.
//     * - sequence.incrementAndGet(): 1씩 증가하는 번호를 만든다. (예: 1, 2, 3, ...)
//     * → 그 다음, store(메모리 저장소)에 "id: member"로 저장한다.
//     * 2. 만약 id가 이미 있다면(수정 용도 등)
//     * → 그냥 그 id로 store에 덮어쓴다.
//     * (이미 있던 회원 정보가 있으면 교체, 없으면 새로 추가)
//     * 3. 마지막으로, 전체 회원 데이터를 파일에도 저장한다(영구 저장).
//     *
//     * 숙지:
//     * - store.put(id, member)는 자바의 Map에서 "id를 key로 member 객체를 저장"한다는 뜻!
//     * - saveDataToFile()을 반드시 호출해야 데이터가 실제 파일에도 반영됨.
//     */
//    @Override
//    public void save(Member member) {
//        // (1) 신규 회원인지 체크: id가 비어있으면 새로 만들어줌
//        if (member.getId() == null) {
//            // sequence: 자동 증가 숫자. 새 id 생성!
//            long newId = sequence.incrementAndGet();
//            member.setId(newId);
//            store.put(newId, member); // 새 회원 저장
//        } else {
//            // 이미 id가 있으면, 해당 id로 저장(덮어쓰기 됨)
//            store.put(member.getId(), member);
//        }
//        // (2) 모든 회원 정보를 파일로 저장(영구화)
//        saveDataToFile();
//    }