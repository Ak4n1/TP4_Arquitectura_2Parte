package com.tudai.monopatines.accounts.accounts_services.filter;

import com.tudai.monopatines.accounts.accounts_services.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    
    // Clave de la cabecera de la peticion http.
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    // Prefijo para el header Authorization
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Metodo principal del filtro que se ejecuta en cada request.
     * 
     * Si el token no existe, es invalido o esta expirado, el request continua sin autenticacion
     * y sera manejado por las reglas de autorizacion en SecurityConfig.
     * 
     * @param request Request HTTP entrante
     * @param response Response HTTP saliente
     * @param filterChain Cadena de filtros para continuar el procesamiento
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraer el header Authorization
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Verificar que el header exista y tenga el formato correcto "Bearer <token>"
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            // Extraer el token (eliminar el prefijo "Bearer ")
            String token = authHeader.substring(BEARER_PREFIX.length());

            // Validar el token (firma, expiracion, formato)
            if (jwtService.validateToken(token)) {
                // Extraer informacion del token
                Long userId = jwtService.getUserIdFromToken(token);
                List<String> roles = jwtService.getRolesFromToken(token);

                // Convertir roles (String) a SimpleGrantedAuthority para Spring Security
                // Ejemplo: "ROLE_USER" -> SimpleGrantedAuthority("ROLE_USER")
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Crear objeto de autenticacion con:
                // - Principal: userId (identidad del usuario)
                // - Credentials: null (no se guardan credenciales)
                // - Authorities: lista de roles (permisos)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
                // Establecer la autenticacion en el contexto de Spring Security
                // Esto permite que SecurityConfig verifique los permisos segun roles
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // Si el token es invalido, no se establece autenticacion
            // El request continuara sin autenticacion y sera rechazado si requiere autenticacion
        }
        // Si no hay header Authorization, el request continua sin autenticacion

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}

