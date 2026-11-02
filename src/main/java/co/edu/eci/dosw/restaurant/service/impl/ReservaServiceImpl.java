package co.edu.eci.dosw.restaurant.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.dto.request.ReservaRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ReservaResponseDTO;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.exception.ValidacionException;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;
import co.edu.eci.dosw.restaurant.persistence.entity.ReservaEntity;
import co.edu.eci.dosw.restaurant.repository.ReservaRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import co.edu.eci.dosw.restaurant.service.IReservaService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final IAuditoriaService auditoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarTodas() {
        log.info("Consultando reservas (fecha y hora descendente)");
        return reservaRepository.findAllByOrderByFechaDescHoraDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReservaResponseDTO crear(ReservaRequestDTO solicitud) {
        if (solicitud.getFecha().isBefore(LocalDate.now())) {
            throw new ValidacionException("La fecha de la reserva no puede estar en el pasado");
        }

        ReservaEntity entity = ReservaEntity.builder()
                .nombreCliente(solicitud.getNombreCliente().trim())
                .emailCliente(solicitud.getEmailCliente().trim())
                .telefono(solicitud.getTelefono() == null ? null : solicitud.getTelefono().trim())
                .fecha(solicitud.getFecha())
                .hora(solicitud.getHora())
                .numeroPersonas(solicitud.getNumeroPersonas())
                .mesaNumero(solicitud.getMesaNumero())
                .zona(solicitud.getZona())
                .notas(solicitud.getNotas())
                .estado(EstadoReserva.CONFIRMADA)
                .build();

        ReservaEntity guardada = reservaRepository.save(entity);
        log.info("Reserva creada: ID={}, cliente='{}', fecha={} {}",
                guardada.getId(), guardada.getNombreCliente(), guardada.getFecha(), guardada.getHora());

        registrarAuditoria("RESERVA_CREADA", guardada,
                String.format("Reserva de '%s' para %d personas el %s a las %s",
                        guardada.getNombreCliente(), guardada.getNumeroPersonas(),
                        guardada.getFecha(), guardada.getHora()));

        return toResponse(guardada);
    }

    @Override
    @Transactional
    public ReservaResponseDTO cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        ReservaEntity entity = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));

        entity.setEstado(nuevoEstado);
        ReservaEntity guardada = reservaRepository.save(entity);
        log.info("Reserva ID={} cambió a estado {}", id, nuevoEstado);

        registrarAuditoria("RESERVA_ESTADO_CAMBIADO", guardada,
                String.format("Reserva #%d cambió a estado %s", guardada.getId(), nuevoEstado));

        return toResponse(guardada);
    }

    /** La bitácora documental es de mejor esfuerzo: jamás debe afectar la transacción relacional. */
    private void registrarAuditoria(String tipo, ReservaEntity reserva, String descripcion) {
        try {
            Map<String, Object> metadatos = new HashMap<>();
            metadatos.put("estado", String.valueOf(reserva.getEstado()));
            metadatos.put("fecha", String.valueOf(reserva.getFecha()));
            metadatos.put("hora", reserva.getHora());

            auditoriaService.registrarEvento(EventoRestaurante.builder()
                    .tipo(tipo)
                    .entidadTipo("RESERVA")
                    .entidadId(reserva.getId())
                    .descripcion(descripcion)
                    .usuario("SISTEMA")
                    .timestamp(LocalDateTime.now())
                    .metadatos(metadatos)
                    .build());
        } catch (Exception e) {
            log.warn("Bitácora de reservas no disponible, se omite evento '{}': {}", tipo, e.getMessage());
        }
    }

    private ReservaResponseDTO toResponse(ReservaEntity e) {
        return ReservaResponseDTO.builder()
                .id(e.getId())
                .nombreCliente(e.getNombreCliente())
                .emailCliente(e.getEmailCliente())
                .telefono(e.getTelefono())
                .fecha(e.getFecha())
                .hora(e.getHora())
                .numeroPersonas(e.getNumeroPersonas())
                .mesaNumero(e.getMesaNumero())
                .zona(e.getZona())
                .estado(e.getEstado())
                .notas(e.getNotas())
                .creadoEn(e.getCreadoEn())
                .build();
    }
}
