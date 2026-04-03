package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.todo.entity.Todo;

import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class TodoCustomRepositoryImpl implements TodoCustomRepository {
    private final JPAQueryFactory factory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        log.info("Query DSL 실행됨");
        return Optional.ofNullable(
                factory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }
}
