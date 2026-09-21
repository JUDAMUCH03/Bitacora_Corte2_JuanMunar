package co.edu.eci.dosw.restaurant.model.domain;

import co.edu.eci.dosw.restaurant.model.domain.enums.CategoriaBebida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad pura del dominio de Blue Velvet.
 * Modela un producto de la carta sin acoplamiento a infraestructura ni a persistencia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plato {

    private Long id;
    private String nombre;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private String descripcion;

    // ── Comportamiento de negocio intrínseco del objeto ──

    /**
     * Valida si el ítem está activo y listo para ordenar.
     */
    public boolean estaDisponible() {
        return Boolean.TRUE.equals(this.disponible);
    }

    /**
     * Habilita el producto en la carta digital.
     */
    public void activar() {
        this.disponible = true;
    }

    /**
     * Inhabilita el producto (ej. quiebre de stock de un botánico o destilado base).
     */
    public void desactivar() {
        this.disponible = false;
    }

    /**
     * Regla de negocio Blue Velvet: Determina si el producto es catalogado
     * como Mocktail 0.0% ABV para aislarlo de alcohol.
     */
    public boolean esMocktail() {
        if (this.categoria == null) {
            return false;
        }
        return CategoriaBebida.MOCKTAIL.name().equalsIgnoreCase(this.categoria.trim());
    }
}