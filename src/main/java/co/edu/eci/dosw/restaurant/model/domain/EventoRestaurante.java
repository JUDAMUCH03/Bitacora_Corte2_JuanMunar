package co.edu.eci.dosw.restaurant.model.domain;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio para la auditoría y trazabilidad de eventos en Blue Velvet.
 * Desacoplado de cualquier infraestructura o motor NoSQL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoRestaurante {

    private String id;
    private String tipo;
    private String entidadTipo;
    private Long entidadId;
    private String descripcion;
    private String usuario;
    private LocalDateTime timestamp;
    private Map<String, Object> metadatos;
}
