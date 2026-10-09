package co.edu.eci.dosw.restaurant.validator;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.eci.dosw.restaurant.exception.EstadoInvalidoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.exception.ValidacionException;
import co.edu.eci.dosw.restaurant.model.domain.ItemPedido;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

class PedidoValidatorTest {

    private PedidoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PedidoValidator();
    }

    @Test
    @DisplayName("validarCreacion - Pedido sin ítems lanza ValidacionException")
    void validarCreacion_sinItems_lanzaValidacionException() {
        Pedido pedidoVacio = Pedido.builder().items(List.of()).build();
        assertThrows(ValidacionException.class, () -> validator.validarCreacion(pedidoVacio));
    }

    @Test
    @DisplayName("validarCreacion - Ítem con más de 2 garnishes lanza ValidacionException (HTTP 400)")
    void validarCreacion_masDeDosGarnishes_lanzaValidacionException() {
        ItemPedido item = ItemPedido.builder()
                .idPlato(1L)
                .cantidad(1)
                .garnishes(List.of("Cereza", "Rodaja de limón", "Menta"))
                .build();
        Pedido pedido = Pedido.builder().items(List.of(item)).build();

        assertThrows(ValidacionException.class, () -> validator.validarCreacion(pedido));
    }

    @Test
    @DisplayName("validarCreacion - Ítem con hasta 2 garnishes pasa validación")
    void validarCreacion_dosGarnishesOMenos_pasa() {
        ItemPedido item = ItemPedido.builder()
                .idPlato(1L)
                .cantidad(1)
                .garnishes(List.of("Rodaja de limón", "Menta"))
                .build();
        Pedido pedido = Pedido.builder().items(List.of(item)).build();

        assertDoesNotThrow(() -> validator.validarCreacion(pedido));
    }

    @Test
    @DisplayName("validarTransicionEstado - Cancelar pedido en estado EN_PREPARACION lanza ReglaDeNegocioException (HTTP 422)")
    void validarTransicionEstado_cancelarEnPreparacion_lanzaReglaDeNegocioException() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.EN_PREPARACION).build();

        assertThrows(ReglaDeNegocioException.class, () ->
                validator.validarTransicionEstado(pedido, EstadoPedido.CANCELADO));
    }

    @Test
    @DisplayName("validarTransicionEstado - Cancelar pedido en estado RECIBIDO es permitido")
    void validarTransicionEstado_cancelarEnRecibido_pasa() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.RECIBIDO).build();

        assertDoesNotThrow(() -> validator.validarTransicionEstado(pedido, EstadoPedido.CANCELADO));
    }

    @Test
    @DisplayName("validarTransicionEstado - Transición inválida KDS lanza EstadoInvalidoException (HTTP 422)")
    void validarTransicionEstado_transicionInvalida_lanzaEstadoInvalidoException() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.RECIBIDO).build();

        assertThrows(EstadoInvalidoException.class, () ->
                validator.validarTransicionEstado(pedido, EstadoPedido.ENTREGADO));
    }

    @Test
    @DisplayName("validarTransicionEstado - Transición secuencial válida pasa")
    void validarTransicionEstado_transicionValida_pasa() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.RECIBIDO).build();
        assertDoesNotThrow(() -> validator.validarTransicionEstado(pedido, EstadoPedido.EN_PREPARACION));

        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        assertDoesNotThrow(() -> validator.validarTransicionEstado(pedido, EstadoPedido.LISTO));

        pedido.setEstado(EstadoPedido.LISTO);
        assertDoesNotThrow(() -> validator.validarTransicionEstado(pedido, EstadoPedido.ENTREGADO));
    }

    @Test
    @DisplayName("validarModificacionDestilado - Modificar destilado en estado EN_PREPARACION lanza ReglaDeNegocioException")
    void validarModificacionDestilado_enPreparacion_lanzaReglaDeNegocioException() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.EN_PREPARACION).build();
        ItemPedido item = ItemPedido.builder().spiritModificationsCount(0).build();

        assertThrows(ReglaDeNegocioException.class, () ->
                validator.validarModificacionDestilado(pedido, item));
    }

    @Test
    @DisplayName("validarModificacionDestilado - Modificar destilado más de 1 vez lanza ReglaDeNegocioException")
    void validarModificacionDestilado_segundaModificacion_lanzaReglaDeNegocioException() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.RECIBIDO).build();
        ItemPedido item = ItemPedido.builder().spiritModificationsCount(1).build();

        assertThrows(ReglaDeNegocioException.class, () ->
                validator.validarModificacionDestilado(pedido, item));
    }

    @Test
    @DisplayName("validarModificacionDestilado - Primera mutación en estado RECIBIDO es permitida")
    void validarModificacionDestilado_primeraModificacionEnRecibido_pasa() {
        Pedido pedido = Pedido.builder().estado(EstadoPedido.RECIBIDO).build();
        ItemPedido item = ItemPedido.builder().spiritModificationsCount(0).build();

        assertDoesNotThrow(() -> validator.validarModificacionDestilado(pedido, item));
    }
}
