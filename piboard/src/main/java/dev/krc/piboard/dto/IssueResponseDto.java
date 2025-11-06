package dev.krc.piboard.dto;

import dev.krc.piboard.model.IssueState;
import lombok.*;

import java.time.LocalDate;

@Data
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
