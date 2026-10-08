package co.edu.eci.dosw.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para mutar la marca de destilado base de un cóctel")
public class ModificarDestiladoDTO {

    @NotNull(message = "El nuevo ID de destilado es obligatorio")
    @Schema(description = "ID del nuevo destilado a aplicar", example = "25")
    private Long nuevoSpiritBrandId;
}
