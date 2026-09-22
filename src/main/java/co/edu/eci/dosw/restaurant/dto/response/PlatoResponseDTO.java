package co.edu.eci.dosw.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Blue Velvet.
 * Define la estructura visible para el cliente y el KDS.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private String descripcion;
}