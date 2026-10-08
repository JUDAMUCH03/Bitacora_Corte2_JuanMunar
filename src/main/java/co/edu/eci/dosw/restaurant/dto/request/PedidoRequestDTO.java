package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Solicitud para registrar una nueva comanda en KDS")
public class PedidoRequestDTO {

    @NotNull(message = "El id de la mesa es obligatorio")
    @Schema(description = "ID de la mesa asociada a la comanda", example = "5")
    private Long idMesa;

    @NotEmpty(message = "La comanda debe contener al menos un ítem")
    @Valid
    @Schema(description = "Lista de ítems o bebidas solicitados")
    private List<ItemPedidoRequestDTO> items;
}
