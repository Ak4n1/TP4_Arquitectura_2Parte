package com.tudai.monopatines.auth_service.service;

import com.tudai.monopatines.auth_service.client.AccountsServiceClient;
import com.tudai.monopatines.auth_service.dto.*;
import com.tudai.monopatines.auth_service.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación.
 */
@Service
public class AuthService {

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param request LoginRequest con email y password
     * @return LoginResponse con token, userId, email y roles
     * @throws InvalidCredentialsException si las credenciales son inválidas
     */
    public LoginResponse login(LoginRequest request) {
        // Validar password primero
        boolean isValid = accountsServiceClient.validatePassword(request.getEmail(), request.getPassword());
        if (!isValid) {
            throw InvalidCredentialsException.forEmail(request.getEmail());
        }

        // Obtener usuario desde accounts-service (con roles)
        UserResponse user = accountsServiceClient.getUserByEmail(request.getEmail());

        // Generar token con userId y roles
        String token = jwtService.generateToken(user.getId(), user.getRoles());

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getRoles());
    }

    /**
     * Registra un nuevo usuario.
     * 
     * @param request RegisterRequest con los datos del usuario
     * @return RegisterResponse con userId, email y mensaje
     * @throws RuntimeException si el usuario ya existe
     */
    public RegisterResponse register(RegisterRequest request) {
        // Hashear password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Crear request para accounts-service
        CreateUserRequest createUserRequest = new CreateUserRequest(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                hashedPassword
        );

        // Crear usuario en accounts-service
        UserResponse user = accountsServiceClient.createUser(createUserRequest);

        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                "User registered successfully"
        );
    }
}

