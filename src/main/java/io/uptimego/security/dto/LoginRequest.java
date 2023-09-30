package io.uptimego.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "{login_username_not_empty}")
    private String email;

    @NotEmpty(message = "{login_password_not_empty}")
    private String password;

}