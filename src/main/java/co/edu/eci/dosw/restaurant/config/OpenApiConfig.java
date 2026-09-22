package co.edu.eci.dosw.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de OpenAPI 3 / Swagger para Blue Velvet.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blueVelvetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🍸 Blue Velvet — Coctelería de Autor API")
                        .description("API REST transaccional para la gestión de carta digital, pedidos y KDS de barra.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Juan Munar — DOSW")
                                .email("juan.munar@mail.escuelaing.edu.co")));
    }
}