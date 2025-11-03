package dev.krc.piboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssueRequestDto {
    private String title;
    private String description;
    private LocalDate dueDate;
}
