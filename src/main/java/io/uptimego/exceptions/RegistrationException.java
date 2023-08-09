package io.uptimego.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegistrationException extends RuntimeException {

    private final String errorMessage;

}