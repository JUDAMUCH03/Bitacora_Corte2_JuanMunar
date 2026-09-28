package co.edu.eci.dosw.restaurant.service.impl;

import co.edu.eci.dosw.restaurant.mapper.EventoMapper;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument;
import co.edu.eci.dosw.restaurant.repository.EventoRestauranteRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements IAuditoriaService {

    private final EventoRestauranteRepository eventoRepository;
    private final EventoMapper eventoMapper;

    @Override
    public EventoRestaurante registrarEvento(EventoRestaurante evento) {
        log.info("Registrando evento de auditoría NoSQL: tipo='{}', entidadTipo='{}', entidadId={}",
                evento.getTipo(), evento.getEntidadTipo(), evento.getEntidadId());

        if (evento.getTimestamp() == null) {
            evento.setTimestamp(LocalDateTime.now());
        }

        EventoRestauranteDocument documento = eventoMapper.toDocument(evento);
        EventoRestauranteDocument guardado = eventoRepository.save(documento);

        log.debug("Evento persistido en MongoDB con id={}", guardado.getId());
        return eventoMapper.toDomain(guardado);
    }

    @Override
    public List<EventoRestaurante> obtenerEventosPorEntidad(String entidadTipo, Long entidadId) {
        log.debug("Consultando eventos de auditoría para entidadTipo='{}', entidadId={}", entidadTipo, entidadId);
        return eventoRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId).stream()
                .map(eventoMapper::toDomain)
                .toList();
    }

    @Override
    public List<EventoRestaurante> obtenerEventosPorTipoYRango(String tipo, LocalDateTime desde, LocalDateTime hasta) {
        log.debug("Consultando eventos por tipo='{}' entre {} y {}", tipo, desde, hasta);
        return eventoRepository.findByTipoAndTimestampBetween(tipo, desde, hasta).stream()
                .map(eventoMapper::toDomain)
                .toList();
    }
}
