package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Record DTO para respuesta de autenticación con token JWT en Blue Velvet.
 */
@Schema(description = "DTO de respuesta que contiene el token JWT de sesión")
public record TokenResponseDTO(
        @Schema(description = "Token de acceso Bearer JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
    public String getToken() {
        return token;
    }
}
