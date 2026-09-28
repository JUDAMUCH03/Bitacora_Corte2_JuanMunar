package co.edu.eci.dosw.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;

/**
 * Contrato de servicio para la gestión de auditoría y trazabilidad de eventos en MongoDB.
 */
public interface IAuditoriaService {

    EventoRestaurante registrarEvento(EventoRestaurante evento);

    List<EventoRestaurante> obtenerEventosPorEntidad(String entidadTipo, Long entidadId);

    List<EventoRestaurante> obtenerEventosPorTipoYRango(String tipo, LocalDateTime desde, LocalDateTime hasta);
}
