package org.example.apigateway.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthJwtException extends AuthenticationException {
    public AuthJwtException(String msg) {
        super(msg);
    }
}

