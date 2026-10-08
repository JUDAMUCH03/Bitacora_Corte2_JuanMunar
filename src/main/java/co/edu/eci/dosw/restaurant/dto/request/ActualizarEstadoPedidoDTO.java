package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para cambiar de estado un pedido en KDS")
public class ActualizarEstadoPedidoDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado en el flujo del KDS", example = "EN_PREPARACION")
    private EstadoPedido nuevoEstado;
}
