package com.tudai.monopatines.auth_service.exception;

/**
 * Excepción lanzada cuando las credenciales de login son inválidas.
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public static InvalidCredentialsException forEmail(String email) {
        return new InvalidCredentialsException("Invalid email or password for user: " + email);
    }
}

