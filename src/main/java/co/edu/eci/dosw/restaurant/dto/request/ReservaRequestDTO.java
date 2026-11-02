package co.edu.eci.dosw.restaurant.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud de reserva de mesa")
public class ReservaRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    private String nombreCliente;

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    private String emailCliente;

    @Size(max = 40, message = "El teléfono no puede superar 40 caracteres")
    private String telefono;

    @NotNull(message = "La fecha de la reserva es obligatoria")
    @Schema(description = "Fecha en formato ISO yyyy-MM-dd", example = "2026-11-20")
    private LocalDate fecha;

    @NotBlank(message = "La hora de la reserva es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm")
    @Schema(description = "Hora en formato HH:mm", example = "20:30")
    private String hora;

    @NotNull(message = "El número de personas es obligatorio")
    @Min(value = 1, message = "Debe reservar para al menos 1 persona")
    @Max(value = 20, message = "El máximo por reserva es de 20 personas")
    private Integer numeroPersonas;

    @Min(value = 1, message = "El número de mesa debe ser positivo")
    private Integer mesaNumero;

    @Size(max = 60, message = "La zona no puede superar 60 caracteres")
    private String zona;

    @Size(max = 500, message = "Las notas no pueden superar 500 caracteres")
    private String notas;
}
