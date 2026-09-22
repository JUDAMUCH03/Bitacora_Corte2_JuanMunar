package co.edu.eci.dosw.restaurant.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatoTest {

    @Test
    @DisplayName("estaDisponible - evalúa correctamente el booleano")
    void estaDisponible_evaluaCorrectamente() {
        Plato p1 = Plato.builder().disponible(true).build();
        Plato p2 = Plato.builder().disponible(false).build();
        Plato p3 = Plato.builder().disponible(null).build();

        assertTrue(p1.estaDisponible());
        assertFalse(p2.estaDisponible());
        assertFalse(p3.estaDisponible());
    }

    @Test
    @DisplayName("activar y desactivar - mutan el estado adecuadamente")
    void activarYDesactivar_cambianEstado() {
        Plato plato = new Plato();
        plato.activar();
        assertTrue(plato.estaDisponible());

        plato.desactivar();
        assertFalse(plato.estaDisponible());
    }

    @Test
    @DisplayName("esMocktail - reconoce la categoría 0.0% ABV")
    void esMocktail_validaCategoria() {
        Plato mocktail = Plato.builder().categoria("MOCKTAIL").build();
        Plato coctel = Plato.builder().categoria("COCTEL_AUTOR").build();
        Plato sinCategoria = new Plato();

        assertTrue(mocktail.esMocktail());
        assertFalse(coctel.esMocktail());
        assertFalse(sinCategoria.esMocktail());
    }
}