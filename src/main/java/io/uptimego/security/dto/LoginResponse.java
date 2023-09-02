package io.uptimego.security.dto;

import io.uptimego.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    private User userData;

}
