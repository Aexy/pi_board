package dev.krc.piboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private String email;
    private String password;
}
