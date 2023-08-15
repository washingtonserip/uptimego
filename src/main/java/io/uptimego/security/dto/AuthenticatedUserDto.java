package io.uptimego.security.dto;

import io.uptimego.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private String name;

    private String email;

    private String password;

    private UserRole userRole;

}
