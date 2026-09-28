package co.edu.eci.dosw.restaurant.service.impl;

import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.PlatoEntityMapper;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.persistence.entity.PlatoEntity;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import co.edu.eci.dosw.restaurant.service.IPlatoService;
import co.edu.eci.dosw.restaurant.validator.IPlatoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatoServiceImpl implements IPlatoService {

    private final PlatoRepository platoRepository;
    private final PlatoEntityMapper entityMapper;
    private final IPlatoValidator validator;
    private final IAuditoriaService auditoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<Plato> obtenerTodos() {
        log.info("Consultando catálogo completo de Blue Velvet");
        return platoRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> obtenerDisponibles() {
        log.debug("Consultando cócteles y platos disponibles para sala y barra");
        return platoRepository.findByDisponibleTrue().stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> obtenerPorCategoria(String categoria) {
        log.debug("Filtrando carta por categoría: {}", categoria);
        return platoRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Plato obtenerPorId(Long id) {
        log.debug("Buscando ítem en carta con ID: {}", id);
        return platoRepository.findById(id)
                .map(entityMapper::toDomain)
                .orElseThrow(() -> {
                    log.warn("Ítem no encontrado en carta: id={}", id);
                    return new RecursoNoEncontradoException("Plato/Cóctel", id);
                });
    }

    @Override
    @Transactional
    public Plato crear(Plato plato) {
        log.info("Iniciando creación de nuevo producto: '{}'", plato.getNombre());
        validator.validarNombreUnico(plato.getNombre());

        PlatoEntity entity = entityMapper.toEntity(plato);
        PlatoEntity guardado = platoRepository.save(entity);

        log.info("Producto creado exitosamente: ID={}, Nombre='{}'", guardado.getId(), guardado.getNombre());

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("nombre", guardado.getNombre());
        metadatos.put("precio", guardado.getPrecio());
        metadatos.put("categoria", guardado.getCategoria());

        auditoriaService.registrarEvento(EventoRestaurante.builder()
                .tipo("PLATO_CREADO")
                .entidadTipo("PLATO")
                .entidadId(guardado.getId())
                .descripcion(String.format("Se creó el plato '%s' en la carta", guardado.getNombre()))
                .usuario("SISTEMA")
                .timestamp(LocalDateTime.now())
                .metadatos(metadatos)
                .build());

        return entityMapper.toDomain(guardado);
    }

    @Override
    @Transactional
    public Plato actualizar(Long id, Plato nuevosDatos) {
        log.info("Actualizando producto ID={}", id);
        PlatoEntity entity = platoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ítem no encontrado en carta: id={}", id);
                    return new RecursoNoEncontradoException("Plato/Cóctel", id);
                });

        validator.validarNombreUnico(nuevosDatos.getNombre(), id);

        entity.setNombre(nuevosDatos.getNombre());
        entity.setPrecio(nuevosDatos.getPrecio());
        entity.setCategoria(nuevosDatos.getCategoria());
        entity.setDescripcion(nuevosDatos.getDescripcion());
        if (nuevosDatos.getDisponible() != null) {
            entity.setDisponible(nuevosDatos.getDisponible());
        }

        PlatoEntity guardado = platoRepository.save(entity);
        log.info("Producto ID={} actualizado correctamente", id);
        return entityMapper.toDomain(guardado);
    }

    @Override
    @Transactional
    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        log.info("Modificando disponibilidad: ID={} -> disponible={}", id, disponible);
        PlatoEntity entity = platoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ítem no encontrado en carta: id={}", id);
                    return new RecursoNoEncontradoException("Plato/Cóctel", id);
                });

        entity.setDisponible(disponible);
        PlatoEntity guardado = platoRepository.save(entity);

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("nombre", guardado.getNombre());
        metadatos.put("disponible", disponible);

        auditoriaService.registrarEvento(EventoRestaurante.builder()
                .tipo("DISPONIBILIDAD_CAMBIADA")
                .entidadTipo("PLATO")
                .entidadId(guardado.getId())
                .descripcion(String.format("Disponibilidad del plato '%s' cambiada a %s",
                        guardado.getNombre(), disponible ? "disponible" : "no disponible"))
                .usuario("SISTEMA")
                .timestamp(LocalDateTime.now())
                .metadatos(metadatos)
                .build());

        return entityMapper.toDomain(guardado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando producto de la carta: ID={}", id);
        if (!platoRepository.existsById(id)) {
            log.warn("Ítem no encontrado en carta: id={}", id);
            throw new RecursoNoEncontradoException("Plato/Cóctel", id);
        }
        platoRepository.deleteById(id);
        log.info("Producto ID={} eliminado de la persistencia", id);
    }
}