package com.tudai.monopatines.auth_service.client;

import com.tudai.monopatines.auth_service.dto.CreateUserRequest;
import com.tudai.monopatines.auth_service.dto.UserResponse;
import com.tudai.monopatines.auth_service.exception.UserAlreadyExistsException;
import com.tudai.monopatines.auth_service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Cliente HTTP para consumir accounts-service usando RestTemplate.
 */
@Component
public class AccountsServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AccountsServiceClient(RestTemplate restTemplate, @Value("${accounts.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Obtiene un usuario por email desde accounts-service.
     * 
     * @param email Email del usuario
     * @return UserResponse con los datos del usuario y sus roles
     * @throws UserNotFoundException si el usuario no existe
     */
    public UserResponse getUserByEmail(String email) {
        try {
            String url = baseUrl + "/api/accounts/users?email={email}";
            return restTemplate.getForObject(url, UserResponse.class, email);
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException(email, "User not found");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error calling accounts-service: " + e.getMessage());
        }
    }

    /**
     * Crea un usuario en accounts-service.
     * 
     * @param request Datos del usuario a crear (con password ya hasheada)
     * @return UserResponse con el usuario creado
     * @throws UserAlreadyExistsException si el usuario ya existe
     */
    public UserResponse createUser(CreateUserRequest request) {
        try {
            String url = baseUrl + "/api/accounts/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CreateUserRequest> httpEntity = new HttpEntity<>(request, headers);

            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    UserResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException.Conflict e) {
            throw UserAlreadyExistsException.forEmail(request.getEmail());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error creating user in accounts-service: " + e.getMessage());
        }
    }

    /**
     * Valida el password de un usuario.
     * 
     * @param email    Email del usuario
     * @param password Password a validar
     * @return true si el password es válido, false en caso contrario
     * @throws UserNotFoundException si el usuario no existe
     */
    public boolean validatePassword(String email, String password) {
        try {
            String url = baseUrl + "/api/accounts/users/validate-password";

            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request, headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    Boolean.class);

            return response.getBody() != null && response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException(email, "User not found");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error validating password in accounts-service: " + e.getMessage());
        }
    }
}
