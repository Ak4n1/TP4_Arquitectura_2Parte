package com.tudai.monopatines.auth_service.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario en el sistema.
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String email, String reason) {
        super("User with email " + email + " not found: " + reason);
    }
}

