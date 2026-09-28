package co.edu.eci.dosw.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuración global de OpenAPI 3 / Swagger para Blue Velvet con soporte Bearer JWT.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI blueVelvetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🍸 Blue Velvet — Coctelería de Autor API")
                        .description("API REST transaccional para la gestión de carta digital, pedidos y KDS de barra.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Juan Munar — DOSW")
                                .email("juan.munar-c@mail.escuelaing.edu.co")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingrese el token JWT obtenido en el endpoint POST /auth/login")));
    }
}