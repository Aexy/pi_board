package dev.krc.piboard.dto;

import lombok.*;

@Getter
@Setter
@Data
public class UserLoginDto {
    private String email;
    private String password;
}
