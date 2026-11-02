package co.edu.eci.dosw.restaurant.dto.request;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para cambiar el estado de una reserva")
public class ActualizarEstadoReservaDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado de la reserva", example = "CONFIRMADA")
    private EstadoReserva estado;
}
