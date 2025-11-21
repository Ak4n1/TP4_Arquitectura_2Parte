package com.tudai.monopatines.accounts.accounts_services.config;

import com.tudai.monopatines.accounts.accounts_services.exception.JwtAccessDeniedHandler;
import com.tudai.monopatines.accounts.accounts_services.exception.JwtAuthenticationEntryPoint;
import com.tudai.monopatines.accounts.accounts_services.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para accounts-service.
 * Protege endpoints según roles: ROLE_USER, ROLE_EMPLOYEE, ROLE_ADMIN
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos necesarios para login y register (usados por auth-service)
                .requestMatchers(HttpMethod.POST, "/api/accounts/users/validate-password").permitAll() // Para login
                .requestMatchers(HttpMethod.POST, "/api/accounts/users").permitAll() // Para register
                .requestMatchers(HttpMethod.GET, "/api/accounts/users").permitAll() // Para login (obtener usuario por email)
                
                // ROLE_ADMIN - Gestión completa
                .requestMatchers(HttpMethod.POST, "/api/accounts").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/accounts/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}/toggle_status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}/balance/deduct").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/accounts/associate").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/accounts/disassociate").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/users/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/accounts/users/{id}").hasRole("ADMIN")
                
                // ROLE_EMPLOYEE - Lectura amplia
                .requestMatchers(HttpMethod.GET, "/api/accounts").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/active").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/{id}").hasAnyRole("USER", "EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/{id}/balance").hasAnyRole("USER", "EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/{accountId}/users").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/users/{id}").hasAnyRole("USER", "EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/{id}/active").hasAnyRole("EMPLOYEE", "ADMIN") // Para otros microservicios
                
                // ROLE_USER - Sus propios recursos
                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}/balance").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts/users/{userId}/accounts").hasAnyRole("USER", "EMPLOYEE", "ADMIN")
                
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

