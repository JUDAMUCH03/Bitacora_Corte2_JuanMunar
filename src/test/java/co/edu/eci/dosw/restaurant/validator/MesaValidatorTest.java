package co.edu.eci.dosw.restaurant.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoMesa;

class MesaValidatorTest {

    private MesaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MesaValidator();
    }

    @Test
    @DisplayName("validarAperturaCuenta - Mesa con cuenta previamente abierta lanza ConflictoException (HTTP 409)")
    void validarAperturaCuenta_mesaConCuentaAbierta_lanzaConflictoException() {
        Mesa mesaOcupada = Mesa.builder()
                .id(1L)
                .numero(5)
                .capacidad(4)
                .estado(EstadoMesa.OCUPADA)
                .cuentaAbierta(true)
                .build();

        assertThrows(ConflictoException.class, () -> validator.validarAperturaCuenta(mesaOcupada));
    }

    @Test
    @DisplayName("validarAperturaCuenta - Mesa libre sin cuenta abierta pasa validación")
    void validarAperturaCuenta_mesaDisponible_noLanzaExcepcion() {
        Mesa mesaDisponible = Mesa.builder()
                .id(2L)
                .numero(3)
                .capacidad(2)
                .estado(EstadoMesa.DISPONIBLE)
                .cuentaAbierta(false)
                .build();

        assertDoesNotThrow(() -> validator.validarAperturaCuenta(mesaDisponible));
    }

    @Test
    @DisplayName("validarCierreCuenta - Cuenta en estado diferente a ABIERTA lanza ReglaDeNegocioException (HTTP 422)")
    void validarCierreCuenta_cuentaYaPagada_lanzaReglaDeNegocioException() {
        Cuenta cuentaPagada = Cuenta.builder()
                .id(10L)
                .idMesa(1L)
                .estado(EstadoCuenta.PAGADA)
                .total(150.0)
                .build();

        assertThrows(ReglaDeNegocioException.class, () -> validator.validarCierreCuenta(cuentaPagada));
    }

    @Test
    @DisplayName("validarCierreCuenta - Cuenta en estado ABIERTA pasa validación")
    void validarCierreCuenta_cuentaAbierta_noLanzaExcepcion() {
        Cuenta cuentaAbierta = Cuenta.builder()
                .id(11L)
                .idMesa(1L)
                .estado(EstadoCuenta.ABIERTA)
                .total(150.0)
                .build();

        assertDoesNotThrow(() -> validator.validarCierreCuenta(cuentaAbierta));
    }
}
