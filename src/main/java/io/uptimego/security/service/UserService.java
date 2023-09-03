package io.uptimego.security.service;

import io.uptimego.model.User;
import io.uptimego.security.dto.AuthenticatedUserDto;
import io.uptimego.security.dto.RegistrationRequest;
import io.uptimego.security.dto.RegistrationResponse;

public interface UserService {

    User findByUsername(String username);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

    User getCurrentUser();

}