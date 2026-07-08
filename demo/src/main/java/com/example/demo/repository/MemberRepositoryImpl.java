package com.example.demo.repository;

import com.example.demo.domain.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
//@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DATA_FILE_PATH = "data/members.json";
    private final Map<Long, Member> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    public MemberRepositoryImpl() {
        loadDataFromFile();
    }

    private void loadDataFromFile() {
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

    private void saveDataToFile() {
        try {
            List<Member> members = new ArrayList<>(store.values());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), members);
        } catch (IOException e) {
            log.error("회원 데이터 저장 실패", e);
            throw new RuntimeException("데이터 저장 실패", e);
        }
    }

    @Override
    public void save(Member member) {
        if (member.getId() == null) {

            long newId = sequence.incrementAndGet();
            member.setId(newId);

            store.put(newId, member);

        } else {
            store.put(member.getId(), member);
        }
        saveDataToFile();
    }

    @Override
    public Member findByUserId(String userId) {
        return store.values().stream()
                .filter(member -> member.getUserId().equals(userId))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    @Override
    public void remove(Long id) {
        store.remove(id);
        saveDataToFile();
    }
}
