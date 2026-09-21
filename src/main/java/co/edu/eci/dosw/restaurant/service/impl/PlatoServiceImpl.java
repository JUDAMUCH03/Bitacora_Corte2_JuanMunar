package co.edu.eci.dosw.restaurant.service.impl;

import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.service.IPlatoService;
import co.edu.eci.dosw.restaurant.validator.IPlatoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatoServiceImpl implements IPlatoService {

    // Almacenamiento concurrente en memoria (simula persistencia segura multihilo)
    private final Map<Long, Plato> catalogoPlatos = new ConcurrentHashMap<>();
    private final AtomicLong identificadorSecuencia = new AtomicLong(1);

    // Inyección de dependencia vía interfaz (DIP)
    private final IPlatoValidator validator;

    @Override
    public List<Plato> obtenerTodos() {
        log.info("Consultando catálogo completo de Blue Velvet. Total registros: {}", catalogoPlatos.size());
        return catalogoPlatos.values().stream()
                .toList();
    }

    @Override
    public List<Plato> obtenerDisponibles() {
        log.debug("Consultando cócteles y platos disponibles para sala y barra");
        return catalogoPlatos.values().stream()
                .filter(Plato::estaDisponible)
                .toList();
    }

    @Override
    public List<Plato> obtenerPorCategoria(String categoria) {
        log.debug("Filtrando carta por categoría: {}", categoria);
        return catalogoPlatos.values().stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }

    @Override
    public Plato obtenerPorId(Long id) {
        log.debug("Buscando ítem en carta con ID: {}", id);
        return catalogoPlatos.values().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Ítem no encontrado en carta: id={}", id);
                    return new RecursoNoEncontradoException("Plato/Cóctel", id);
                });
    }

    @Override
    public Plato crear(Plato plato) {
        log.info("Iniciando creación de nuevo producto: '{}'", plato.getNombre());
        
        // Validación de regla de negocio
        validator.validarNombreUnico(plato.getNombre(), catalogoPlatos.values());

        long nuevoId = identificadorSecuencia.getAndIncrement();
        plato.setId(nuevoId);
        catalogoPlatos.put(nuevoId, plato);

        log.info("Producto creado exitosamente: ID={}, Nombre='{}'", nuevoId, plato.getNombre());
        return plato;
    }

    @Override
    public Plato actualizar(Long id, Plato nuevosDatos) {
        log.info("Actualizando producto ID={}", id);
        Plato existente = obtenerPorId(id); // Lanza 404 si no existe

        // Validar unicidad excluyendo al propio plato del filtro
        List<Plato> otrosPlatos = catalogoPlatos.values().stream()
                .filter(p -> !p.getId().equals(id))
                .toList();
        validator.validarNombreUnico(nuevosDatos.getNombre(), otrosPlatos);

        existente.setNombre(nuevosDatos.getNombre());
        existente.setPrecio(nuevosDatos.getPrecio());
        existente.setCategoria(nuevosDatos.getCategoria());
        existente.setDescripcion(nuevosDatos.getDescripcion());

        log.info("Producto ID={} actualizado correctamente", id);
        return existente;
    }

    @Override
    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        log.info("Modificando disponibilidad: ID={} -> disponible={}", id, disponible);
        Plato plato = obtenerPorId(id);
        
        if (disponible) {
            plato.activar();
        } else {
            plato.desactivar();
        }
        
        return plato;
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando producto de la carta: ID={}", id);
        obtenerPorId(id); // Valida existencia (lanza 404 si no existe)
        
        catalogoPlatos.remove(id);
        log.info("Producto ID={} eliminado de la memoria", id);
    }
}