package dev.krc.piboard.dto;

import dev.krc.piboard.model.IssueState;
import dev.krc.piboard.model.Issues;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponseDto {

    private int id;

    private String title;
    private String description;
    private LocalDate dueDate;

    private IssueState state;
    private String assignee;

    public static IssueResponseDto fromIssues(Issues issues) {
        return new IssueResponseDto(
                issues.getId(),
                issues.getTitle(),
                issues.getDescription(),
                issues.getDueDate(),
                issues.getState(),
                issues.getUser().getEmail());
    }
}
