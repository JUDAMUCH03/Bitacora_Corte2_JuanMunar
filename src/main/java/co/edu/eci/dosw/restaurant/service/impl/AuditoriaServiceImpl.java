package co.edu.eci.dosw.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.mapper.EventoMapper;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.persistence.document.EventoAuditoria;
import co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument;
import co.edu.eci.dosw.restaurant.repository.AuditoriaRepository;
import co.edu.eci.dosw.restaurant.repository.EventoRestauranteRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements IAuditoriaService {

    private final EventoRestauranteRepository eventoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final EventoMapper eventoMapper;

    @Override
    public EventoRestaurante registrarEvento(EventoRestaurante evento) {
        log.info("Registrando evento de auditoría NoSQL: tipo='{}', entidadTipo='{}', entidadId={}",
                evento.getTipo(), evento.getEntidadTipo(), evento.getEntidadId());

        if (evento.getTimestamp() == null) {
            evento.setTimestamp(LocalDateTime.now());
        }

        try {
            EventoRestauranteDocument documento = eventoMapper.toDocument(evento);
            EventoRestauranteDocument guardado = eventoRepository.save(documento);

            log.debug("Evento persistido en MongoDB con id={}", guardado.getId());
            return eventoMapper.toDomain(guardado);
        } catch (Exception e) {
            log.warn("Auditoría NoSQL no disponible, se omite evento '{}': {}", evento.getTipo(), e.getMessage());
            return evento;
        }
    }

    @Override
    public List<EventoRestaurante> obtenerEventosPorEntidad(String entidadTipo, Long entidadId) {
        log.debug("Consultando eventos de auditoría para entidadTipo='{}', entidadId={}", entidadTipo, entidadId);
        try {
            return eventoRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId).stream()
                    .map(eventoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            log.warn("Fallo al consultar eventos de auditoría por entidad: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<EventoRestaurante> obtenerEventosPorTipoYRango(String tipo, LocalDateTime desde, LocalDateTime hasta) {
        log.debug("Consultando eventos por tipo='{}' entre {} y {}", tipo, desde, hasta);
        try {
            return eventoRepository.findByTipoAndTimestampBetween(tipo, desde, hasta).stream()
                    .map(eventoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            log.warn("Fallo al consultar eventos de auditoría por tipo y rango: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public EventoAuditoria registrarAuditoria(String tipoEvento, String entidadId, String detalle, String usuario) {
        log.info("Registrando evento en auditoría KDS MongoDB: tipo='{}', entidadId='{}', usuario='{}'",
                tipoEvento, entidadId, usuario);

        EventoAuditoria evento = EventoAuditoria.builder()
                .tipoEvento(tipoEvento)
                .entidadId(entidadId)
                .detalle(detalle)
                .usuario(usuario != null ? usuario : "SYSTEM")
                .timestamp(LocalDateTime.now())
                .build();

        try {
            return auditoriaRepository.save(evento);
        } catch (Exception e) {
            log.warn("Auditoría NoSQL no disponible, se omite auditoría KDS '{}': {}", tipoEvento, e.getMessage());
            return evento;
        }
    }

    @Override
    public List<EventoAuditoria> listarUltimosEventos() {
        log.debug("Consultando los últimos 50 eventos de auditoría en MongoDB");
        try {
            return auditoriaRepository.findTop50ByOrderByTimestampDesc();
        } catch (Exception e) {
            log.warn("Fallo al consultar últimos eventos de auditoría en MongoDB: {}", e.getMessage());
            return List.of();
        }
    }
}
