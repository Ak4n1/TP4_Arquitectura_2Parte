package com.tudai.monopatines.auth_service.controller;

import com.tudai.monopatines.auth_service.dto.LoginRequest;
import com.tudai.monopatines.auth_service.dto.LoginResponse;
import com.tudai.monopatines.auth_service.dto.RegisterRequest;
import com.tudai.monopatines.auth_service.dto.RegisterResponse;
import com.tudai.monopatines.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
        summary = "Login de usuario - Endpoint público (no requiere autenticación)",
        description = "Autentica un usuario con email y password, y retorna un token JWT. " +
                "El token incluye el userId y los roles del usuario. " +
                "Este endpoint valida las credenciales llamando a accounts-service."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Registro de usuario - Endpoint público (no requiere autenticación)",
        description = "Registra un nuevo usuario en el sistema. El password se hashea automáticamente " +
                "antes de enviarlo a accounts-service. El usuario creado recibe el rol ROLE_USER por defecto. " +
                "Este endpoint crea el usuario llamando a accounts-service."
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

