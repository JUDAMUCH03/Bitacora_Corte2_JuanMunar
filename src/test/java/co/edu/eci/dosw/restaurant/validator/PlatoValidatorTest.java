package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlatoValidatorTest {

    // Instancia pura bajo prueba: sin contexto de Spring ni dobles
    private final PlatoValidator validator = new PlatoValidator();

    @Test
    @DisplayName("validarNombreUnico - nombre nuevo no registrado no lanza excepción")
    void validarNombreUnico_nombreNuevo_noLanzaExcepcion() {
        List<Plato> existentes = List.of(
                Plato.builder().nombre("Penicillin").build()
        );

        assertDoesNotThrow(() -> validator.validarNombreUnico("Bramble", existentes));
    }

    @Test
    @DisplayName("validarNombreUnico - nombre duplicado (case-insensitive con espacios) lanza ConflictoException")
    void validarNombreUnico_nombreDuplicado_lanzaConflictoException() {
        List<Plato> existentes = List.of(
                Plato.builder().nombre("Penicillin").build()
        );

        assertThrows(ConflictoException.class, () ->
                validator.validarNombreUnico("  penicillin  ", existentes)
        );
    }

    @Test
    @DisplayName("validarDisponibleParaPedido - trago agotado en barra lanza ReglaDeNegocioException")
    void validarDisponibleParaPedido_platoInactivo_lanzaReglaDeNegocioException() {
        Plato agotado = Plato.builder().nombre("Gin Tonic").disponible(false).build();

        assertThrows(ReglaDeNegocioException.class, () ->
                validator.validarDisponibleParaPedido(agotado)
        );
    }

    @Test
    @DisplayName("validarDisponibleParaPedido - trago disponible no lanza excepción")
    void validarDisponibleParaPedido_platoActivo_noLanzaExcepcion() {
        Plato activo = Plato.builder().nombre("Gin Tonic").disponible(true).build();

        assertDoesNotThrow(() -> validator.validarDisponibleParaPedido(activo));
    }
}