package co.edu.eci.dosw.restaurant.service;

import co.edu.eci.dosw.restaurant.model.domain.Plato;

import java.util.List;

/**
 * Contrato del servicio de gestión de carta y coctelería para Blue Velvet.
 * Opera exclusivamente con entidades puras de dominio.
 */
public interface IPlatoService {

    List<Plato> obtenerTodos();

    List<Plato> obtenerDisponibles();

    List<Plato> obtenerPorCategoria(String categoria);

    Plato obtenerPorId(Long id);

    Plato crear(Plato plato);

    Plato actualizar(Long id, Plato nuevosDatos);

    Plato cambiarDisponibilidad(Long id, boolean disponible);

    void eliminar(Long id);
}