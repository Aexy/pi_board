package dev.krc.piboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    private String email;

    private String password;
    private String confirmPassword;
}
