package co.edu.eci.dosw.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación/edición de tragos y platos en Blue Velvet.
 * Contiene únicamente validaciones de formato e input.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser estrictamente mayor a 0")
    private Double precio;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    // Campo opcional: sin @NotNull
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;
}