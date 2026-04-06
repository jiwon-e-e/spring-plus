package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.ManagerLog;
import org.example.expert.domain.manager.repository.ManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerLogService {
    private final ManagerLogRepository managerLogRepository;

    // level 11 : 매니저 등록 요청 시 로그 기록
    // transaction option 을 활용해서 save manager 와 logging 이 개별로 이루어질 수 있도록 하기
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long saveInitialLog(Long loginUserId, Long todoId, Long managerId){
        ManagerLog log = new ManagerLog(loginUserId, todoId, managerId);
        ManagerLog savedLog = managerLogRepository.save(log);
        return savedLog.getId();
    }

    // 해당 요청이 성공했는지 실패했는지,,, 결과값도 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveResultLog(Long logId, boolean status, String message){
        ManagerLog log = managerLogRepository.findById(logId).orElseThrow();
        log.addResult(status, message);
    }
}
