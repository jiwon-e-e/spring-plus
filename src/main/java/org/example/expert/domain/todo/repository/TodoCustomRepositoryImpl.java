package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.todo.dto.request.TodoSummaryRequest;
import org.example.expert.domain.todo.dto.response.TodoSummaryResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class TodoCustomRepositoryImpl implements TodoCustomRepository {
    private final JPAQueryFactory factory;
    private final UserRepository userRepository;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        log.info("Query DSL 실행됨");
        return Optional.ofNullable(
                factory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoSummaryResponse> findAllTodoSummary(TodoSummaryRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(request.getManagerName())) {
            // 매니저들 중 한 명이라도 키워드가 포함되는지
            builder.and(
                    todo.managers.any().user.nickname.contains(request.getManagerName())
            );
        }

        // 제목 일치
        if (request.getTitle() != null && !request.getTitle().isBlank()){
            builder.and(todo.title.eq(request.getTitle()));
        }
        // 생성일 시작점
        if (request.getStartCreated() != null){
            builder.and(todo.modifiedAt.goe(request.getStartCreated()));
        }
        // 생성일 종료점
        if (request.getEndCreated() != null){
            builder.and(todo.modifiedAt.loe(request.getEndCreated()));
        }

        List<TodoSummaryResponse> contents = factory
                .select(Projections.constructor(
                        TodoSummaryResponse.class,
                        todo.title,
                        JPAExpressions
                                .select(manager.count())
                                .from(manager)
                                .where(manager.todo.eq(todo)),
                        JPAExpressions
                                .select(comment.count())
                                .from(comment)
                                .where(comment.todo.eq(todo))
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct().fetch();

        Long total = factory
                .select(todo.countDistinct())
                .from(todo)
                .where(builder)
                .fetchOne();

        if (total == null) total = 0L;

        return new PageImpl<>(contents, pageable, total);
    }
}
