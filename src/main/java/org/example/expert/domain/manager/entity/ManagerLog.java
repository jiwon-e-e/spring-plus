package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "log")
public class ManagerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loginUserId;
    private Long todoId;
    private Long newManagerId;

    private boolean status;
    private String comment;

    private LocalDateTime createdAt;

    public ManagerLog(Long loginUserId, Long todoId, Long newManagerId) {
        this.loginUserId = loginUserId;
        this.todoId = todoId;
        this.newManagerId = newManagerId;
        this.createdAt = LocalDateTime.now();
    }

    public void addResult(boolean status, String comment){
        this.status = status;
        this.comment = comment;
    }
}
