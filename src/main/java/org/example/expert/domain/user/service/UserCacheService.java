package org.example.expert.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserCacheService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // "userCache"라는 이름의 저장소에 결과를 저장.
    // 동일한 nickname으로 호출되면 DB에 안 가고 캐시된 result를 바로 반환!
    @Cacheable(value = "userCache", key = "#nickname")
    public List<Map<String, Object>> getUserByNickname(String nickname) {
        String sql = "SELECT * FROM users WHERE nickname = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql, nickname);
    }
}