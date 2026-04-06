package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSummaryResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoCustomRepository {

    Optional<Todo> findByIdWithUser(Long todoId);

//    Page<TodoSummaryResponse> findAllTodoSummary(
//            String weather,
//            LocalDateTime startModified,
//            LocalDateTime endModified,
//            Pageable pageable);

    Page<TodoSummaryResponse> findAllTodoSummary(
            String managerName,
            LocalDateTime startCreated,
            LocalDateTime endCreated,
            Pageable pageable) ;

    }
