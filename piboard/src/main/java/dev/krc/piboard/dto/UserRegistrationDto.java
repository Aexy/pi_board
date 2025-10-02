package dev.krc.piboard.dto;

import lombok.*;

@Getter
@Setter
@Data
public class UserRegistrationDto {

    private String email;

    private String password;
    private String confirmPassword;
}
