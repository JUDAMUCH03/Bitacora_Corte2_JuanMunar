package co.edu.eci.dosw.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalle de un ítem en una comanda")
public class ItemPedidoResponseDTO {

    @Schema(description = "ID del ítem en la comanda", example = "101")
    private Long id;

    @Schema(description = "ID del plato original", example = "1")
    private Long idPlato;

    @Schema(description = "Nombre congelado del plato", example = "Smoked Old Fashioned")
    private String nombrePlato;

    @Schema(description = "Precio congelado al momento del pedido", example = "38000.0")
    private Double precioCongelado;

    @Schema(description = "Cantidad", example = "2")
    private Integer cantidad;

    @Schema(description = "ID del destilado actual", example = "10")
    private Long spiritBrandId;

    @Schema(description = "Contador de mutaciones de destilado", example = "0")
    private Integer spiritModificationsCount;

    @Schema(description = "Lista de garnishes aplicados")
    private List<String> garnishes;
}
