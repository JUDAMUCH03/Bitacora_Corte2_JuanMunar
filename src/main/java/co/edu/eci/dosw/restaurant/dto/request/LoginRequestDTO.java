package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Record DTO para solicitud de autenticación en Blue Velvet.
 */
@Schema(description = "DTO de solicitud para autenticación y obtención de token JWT")
public record LoginRequestDTO(
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El formato de email no es válido")
        @Schema(description = "Correo electrónico del usuario", example = "admin@bluevelvet.com")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Schema(description = "Contraseña en texto plano", example = "admin123")
        String password
) {
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
