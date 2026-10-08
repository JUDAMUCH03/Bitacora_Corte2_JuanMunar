package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para agregar un ítem o bebida a la comanda")
public class ItemPedidoRequestDTO {

    @NotNull(message = "El id del plato es obligatorio")
    @Schema(description = "ID del plato o cóctel en la carta", example = "1")
    private Long idPlato;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Schema(description = "Cantidad ordenada", example = "2")
    private Integer cantidad;

    @Schema(description = "ID de la marca de destilado elegida (si aplica)", example = "10")
    private Long spiritBrandId;

    @Schema(description = "Lista de garnishes o botánicos (máximo 2)", example = "[\"Rodaja de naranja\", \"Romero flameado\"]")
    private List<String> garnishes;
}
