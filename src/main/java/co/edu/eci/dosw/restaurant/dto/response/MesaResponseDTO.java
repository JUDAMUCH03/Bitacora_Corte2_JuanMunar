package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoMesa;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información y estado de una mesa en sala")
public class MesaResponseDTO {

    @Schema(description = "ID de la mesa", example = "1")
    private Long id;

    @Schema(description = "Número físico de la mesa", example = "5")
    private Integer numero;

    @Schema(description = "Capacidad de comensales", example = "4")
    private Integer capacidad;

    @Schema(description = "Estado de ocupación", example = "DISPONIBLE")
    private EstadoMesa estado;

    @Schema(description = "Indica si la mesa tiene actualmente una cuenta abierta", example = "false")
    private Boolean cuentaAbierta;
}
