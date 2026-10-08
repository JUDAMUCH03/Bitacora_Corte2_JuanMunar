package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Evento de auditoría persistido en MongoDB")
public class EventoAuditoriaResponseDTO {

    @Schema(description = "Identificador único del evento en MongoDB", example = "6524f2b9...")
    private String id;

    @Schema(description = "Tipo de evento de auditoría", example = "CAMBIO_ESTADO_KDS")
    private String tipoEvento;

    @Schema(description = "ID de la entidad vinculada", example = "50")
    private String entidadId;

    @Schema(description = "Detalle de la operación realizada", example = "Pedido cambió de estado a EN_PREPARACION")
    private String detalle;

    @Schema(description = "Usuario responsable", example = "chef@bluevelvet.com")
    private String usuario;

    @Schema(description = "Fecha y hora de ocurrencia", example = "2026-10-08T18:35:00")
    private LocalDateTime timestamp;
}
