package com.tudai.monopatines.auth_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para documentar los endpoints REST.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la información de la API para Swagger/OpenAPI.
     * 
     * Define el título, descripción y versión que aparecerá en la documentación
     * interactiva de Swagger UI.
     * 
     * @return OpenAPI con la configuración de la documentación
     */
    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")
                        .description("API REST para autenticación y registro de usuarios. " +
                                "Permite realizar login y registro de usuarios, generando tokens JWT " +
                                "para la autenticación en otros microservicios del sistema de monopatines eléctricos.")
                        .version("v1.0.0"));
    }
}

