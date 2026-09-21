package co.edu.eci.dosw.restaurant.model.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsDominioTest {

    @Test
    @DisplayName("CategoriaBebida - valida lógica de alcohol y valores existentes")
    void categoriaBebida_evaluaGraduacionYValores() {
        assertTrue(CategoriaBebida.COCTEL_AUTOR.esAlcoholico());
        assertTrue(CategoriaBebida.DESTILADO_PREMIUM.esAlcoholico());
        assertFalse(CategoriaBebida.MOCKTAIL.esAlcoholico());
        assertFalse(CategoriaBebida.BOTANICO.esAlcoholico());

        assertEquals(4, CategoriaBebida.values().length);
        assertEquals(CategoriaBebida.MOCKTAIL, CategoriaBebida.valueOf("MOCKTAIL"));
    }

    @Test
    @DisplayName("EstadoMesa y EstadoCuenta - valida existencia de constantes")
    void estadosMesaYCuenta_cubrenConstantes() {
        assertEquals(3, EstadoMesa.values().length);
        assertEquals(EstadoMesa.DISPONIBLE, EstadoMesa.valueOf("DISPONIBLE"));

        assertEquals(3, EstadoCuenta.values().length);
        assertEquals(EstadoCuenta.ABIERTA, EstadoCuenta.valueOf("ABIERTA"));
    }
}