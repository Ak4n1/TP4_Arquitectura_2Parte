package com.tudai.monopatines.auth_service.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un email que ya existe.
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public static UserAlreadyExistsException forEmail(String email) {
        return new UserAlreadyExistsException("User with email " + email + " already exists");
    }
}

