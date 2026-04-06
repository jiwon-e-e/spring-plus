package org.example.expert.domain.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Before;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.domain.user.service.UserCacheService;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
//@EnableCaching
public class MassDataProcessingTest {
    private static final int COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final String TARGET_NICKNAME = "user_4999999";


    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 500 만건 데이터 삽입
    @BeforeAll
    void initData() {
        // 테이블 초기화 및 스키마 설정
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("CREATE TABLE users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nickname VARCHAR(255))");

        for (int i = 0; i < COUNT / BATCH_SIZE; i++) {
            final int offset = i * BATCH_SIZE;
            String sql = "INSERT INTO users (nickname) VALUES (?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, "user_" + (offset + i));
                }
                @Override
                public int getBatchSize() {
                    return BATCH_SIZE;
                }
            });
        }
    }

    @AfterAll
    void clean() {
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }

    @Test
    @DisplayName("test1: 1번 조회 - 아무런 추가 없음")
    void test_case01(){
        try {
            jdbcTemplate.execute("ALTER TABLE users DROP INDEX idx_nickname");
        } catch (Exception e) {
            // 인덱스가 없어서 에러가 나더라도 무시하고 진행
            System.out.println("기존 인덱스가 없어 삭제를 건너뜁니다.");
        }

        long startTime = System.currentTimeMillis();

        // LIMIT 1을 추가하여 하나만 찾으면 멈추도록 설정
        String sql = "SELECT * FROM users WHERE nickname = ? LIMIT 1";

        // queryForList를 사용하여 데이터가 없을 경우 예외 방지
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, TARGET_NICKNAME);

        long endTime = System.currentTimeMillis();
        System.out.println("===== test1: 1번 조회 - 아무런 추가 없음 =====");
        System.out.println("실제로 값을 찾았는지 확인: " + result.size());
        System.out.println("소요시간: "+ (endTime- startTime) +"\n");
    }

    @Test
    @DisplayName("test2: 1번 조회 - nickname 에 idx 추가")
    void test_case02(){
        jdbcTemplate.execute("CREATE INDEX idx_nickname ON users(nickname)");

        long startTime = System.currentTimeMillis();

        String sql = "SELECT * FROM users WHERE nickname = ? LIMIT 1";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, TARGET_NICKNAME);

        long endTime = System.currentTimeMillis();
        System.out.println("===== test2: 1번 조회 - nickname 에 idx 추가 =====");
        System.out.println("실제로 값을 찾았는지 확인: " + result.size());
        System.out.println("소요시간: "+ (endTime- startTime) +"\n");
    }

    @Test
    @DisplayName("test3: 여러번 조회 - 아무런 추가 없음")
    void test_case03(){
        try {
            jdbcTemplate.execute("ALTER TABLE users DROP INDEX idx_nickname");
        } catch (Exception e) {
            // 인덱스가 없어서 에러가 나더라도 무시하고 진행
            System.out.println("기존 인덱스가 없어 삭제를 건너뜁니다.");
        }

        long startTime = System.currentTimeMillis();

        int cnt = 0;
        while(cnt < 5){
            List<Map<String, Object>> result = userService.getUserByNickname(TARGET_NICKNAME);
            System.out.println("cnt: "+cnt+" | result: "+result);
            cnt++;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("===== test3: 여러번 조회 - 아무런 추가 없음 =====");
        System.out.println("소요시간: "+ (endTime- startTime) +"\n");
    }


    @Test
    @DisplayName("test4: 여러번 조회 - nickname 에 idx 추가")
    void test_case04(){
        jdbcTemplate.execute("CREATE INDEX idx_nickname ON users(nickname)");

        long startTime = System.currentTimeMillis();

        int cnt = 0;
        while(cnt < 100){
            List<Map<String, Object>> result = userService.getUserByNickname(TARGET_NICKNAME);
//            System.out.println("cnt: "+cnt+" | result: "+result);
            cnt++;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("===== test4: 여러번 조회 - nickname 에 idx 추가 =====");
        System.out.println("소요시간: "+ (endTime- startTime) +"\n");
    }

    @Test
    @DisplayName("test5: 여러번 조회 - nickname 에 idx 추가, 캐싱")
    void test_case05(){
        jdbcTemplate.execute("CREATE INDEX idx_nickname ON users(nickname)");

        long startTime = System.currentTimeMillis();

        int cnt = 0;
        while(cnt < 100){
            List<Map<String, Object>> result = userCacheService.getUserByNickname(TARGET_NICKNAME);
//            System.out.println("cnt: "+cnt+" | result: "+result);
            cnt++;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("===== test5: 여러번 조회 - nickname 에 idx 추가, 캐싱 =====");
        System.out.println("소요시간: "+ (endTime- startTime) +"\n");
    }


}