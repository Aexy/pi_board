package dev.krc.piboard.dto;

import dev.krc.piboard.model.IssueState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponseDto {

    private String title;
    private String description;
    private LocalDate dueDate;

    private IssueState state;
    private String assignee;
}
