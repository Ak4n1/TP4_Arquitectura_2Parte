package com.tudai.monopatines.accounts.accounts_services.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI (Swagger) para documentar los endpoints REST.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la informacion de la API para Swagger/OpenAPI.
     * 
     * Define el titulo, descripcion, version y otra informacion de contacto
     * que aparecera en la documentacion interactiva de Swagger UI.
     * 
     * @return OpenAPI con la configuracion de la documentacion
     */
    @Bean
    public OpenAPI accountsServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Accounts Service API")
                        .description("API REST para gestionar cuentas y usuarios del sistema de monopatines electricos. " +
                                "Permite realizar operaciones CRUD sobre cuentas y usuarios, gestionar saldos, " +
                                "asociar usuarios a cuentas, y administrar roles de usuarios. " +
                                "Los roles se muestran en las respuestas pero no se validan en la primera parte del TP. " +
                                "NOTA: Este servicio expone endpoints REST publicos para la primera parte del TP.")
                        .version("v1.0.0"));
    }
}

