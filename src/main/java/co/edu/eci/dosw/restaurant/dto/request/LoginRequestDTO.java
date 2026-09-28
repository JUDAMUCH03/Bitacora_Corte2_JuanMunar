package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de solicitud para autenticación y obtención de token JWT")
public class LoginRequestDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato de email no es válido")
    @Schema(description = "Correo electrónico del usuario", example = "admin@bluevelvet.com")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Schema(description = "Contraseña en texto plano", example = "admin123")
    private String password;
}
