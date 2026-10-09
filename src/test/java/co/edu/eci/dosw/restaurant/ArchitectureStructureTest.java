package co.edu.eci.dosw.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ArchitectureStructureTest {

    @Test
    void shouldHaveExpectedPackageStructure() {
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.service.impl.PlatoServiceImpl"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.service.impl.AuditoriaServiceImpl"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.service.IAuditoriaService"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.controller.docs.PlatoApi"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.controller.AuthController"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.request.LoginRequestDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.response.TokenResponseDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.PlatoMapperIn"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.PlatoMapperOut"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.PlatoEntityMapper"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.EventoMapper"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.persistence.entity.PlatoEntity"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.persistence.entity.UsuarioEntity"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.repository.PlatoRepository"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.repository.UsuarioRepository"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.repository.EventoRestauranteRepository"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.security.JwtUtil"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.security.JwtAuthFilter"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.security.UserDetailsServiceImpl"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.config.SecurityConfig"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.config.CorsConfig"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.config.SwaggerConfig"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.security.UsuarioDetailsService"));
    }
}
