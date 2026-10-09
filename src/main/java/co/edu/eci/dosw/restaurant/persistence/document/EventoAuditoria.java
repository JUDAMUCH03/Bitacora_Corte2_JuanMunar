package co.edu.eci.dosw.restaurant.persistence.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Documento de MongoDB para registro y trazabilidad de eventos de auditoría y KDS.
 */
@Document(collection = "eventos_restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoAuditoria {

    @Id
    private String id;

    private String tipoEvento; // CAMBIO_ESTADO_KDS, CREACION_PEDIDO, MODIFICACION_CARTA, etc.
    private String entidadId;
    private String detalle;
    private String usuario;
    private LocalDateTime timestamp;
}
