package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta detallada de una comanda de KDS")
public class PedidoResponseDTO {

    @Schema(description = "ID del pedido", example = "50")
    private Long id;

    @Schema(description = "ID de la mesa", example = "3")
    private Long idMesa;

    @Schema(description = "Estado actual en la máquina de estados del KDS", example = "RECIBIDO")
    private EstadoPedido estado;

    @Schema(description = "Marca de tiempo de registro", example = "2026-10-08T18:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Ítems que componen la comanda")
    private List<ItemPedidoResponseDTO> items;
}
