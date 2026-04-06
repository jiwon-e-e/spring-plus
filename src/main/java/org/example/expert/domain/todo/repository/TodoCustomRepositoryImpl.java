package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.todo.dto.response.TodoSummaryResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public Page<TodoSummaryResponse> findAllTodoSummary(String managerName, LocalDateTime startCreated, LocalDateTime endCreated, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (managerName != null && !managerName.isBlank()){
            List<User> users = userRepository.findByUsername(managerName);
            System.out.println("*** " +users.get(0).getId());

            if (!users.isEmpty()) {
                builder.and(
                        todo.managers.any().id.in(
                                users.stream()
                                        .map(User::getId)
                                        .toList()
                        )
                );
            }
        }
        if (startCreated != null){
            builder.and(todo.modifiedAt.goe(startCreated));
        }
        if (endCreated != null){
            builder.and(todo.modifiedAt.loe(endCreated));
        }

//        List<TodoSummaryResponse> contents = factory
//                .select(Projections.constructor(TodoSummaryResponse.class,
//                        todo.contents,
//                        todo.managers.size(),
//                        todo.comments.size()
//                        ))
//                .from(todo)
//                .where(builder)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
        List<TodoSummaryResponse> contents = factory
                .select(Projections.constructor(
                        TodoSummaryResponse.class,
                        todo.contents,
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
                .fetch();

        Long total = factory
                .select(todo.count())
                .from(todo)
                .where(builder)
                .fetchOne();

        if (total == null) total = 0L;

        return new PageImpl<>(contents, pageable, total);
    }
}
