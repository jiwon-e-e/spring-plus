package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoSummaryRequest {
    private String title;
    private String managerName;
    private LocalDateTime startCreated;
    private LocalDateTime endCreated;
}
