package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una cuenta o comanda liquidable")
public class CuentaResponseDTO {

    @Schema(description = "ID de la cuenta", example = "10")
    private Long id;

    @Schema(description = "ID de la mesa asociada", example = "1")
    private Long idMesa;

    @Schema(description = "Monto total acumulado", example = "125000.0")
    private Double total;

    @Schema(description = "Estado actual de la cuenta", example = "ABIERTA")
    private EstadoCuenta estado;

    @Schema(description = "Fecha y hora de apertura de la cuenta", example = "2026-10-08T18:00:00")
    private LocalDateTime fechaApertura;
}
