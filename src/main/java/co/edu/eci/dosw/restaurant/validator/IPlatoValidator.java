package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.model.domain.Plato;

/**
 * Contrato de validaciones de reglas de negocio para el catálogo de Blue Velvet.
 */
public interface IPlatoValidator {

    /**
     * Valida que no exista otro trago/plato con el mismo nombre en la carta (case-insensitive).
     */
    void validarNombreUnico(String nombre);

    /**
     * Valida que no exista otro trago/plato con el mismo nombre excluyendo el id especificado (case-insensitive).
     */
    void validarNombreUnico(String nombre, Long id);

    /**
     * Valida que el trago esté activo antes de poder ser asignado a una comanda.
     */
    void validarDisponibleParaPedido(Plato plato);
}