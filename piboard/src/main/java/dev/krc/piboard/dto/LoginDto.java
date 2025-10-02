package dev.krc.piboard.dto;

import lombok.*;

@Getter
@Setter
@Data
public class LoginDto {
    private String email;
    private String password;
}
