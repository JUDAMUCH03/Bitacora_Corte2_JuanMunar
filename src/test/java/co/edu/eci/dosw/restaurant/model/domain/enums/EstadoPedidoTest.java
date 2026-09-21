package co.edu.eci.dosw.restaurant.model.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPedidoTest {

    @Test
    @DisplayName("puedeTransicionarA - valida flujo unidireccional estricto del KDS")
    void transicionesKds_respetanFlujo() {
        assertTrue(EstadoPedido.RECIBIDO.puedeTransicionarA(EstadoPedido.EN_PREPARACION));
        assertTrue(EstadoPedido.RECIBIDO.puedeTransicionarA(EstadoPedido.CANCELADO));
        assertFalse(EstadoPedido.RECIBIDO.puedeTransicionarA(EstadoPedido.ENTREGADO));

        assertTrue(EstadoPedido.EN_PREPARACION.puedeTransicionarA(EstadoPedido.LISTO));
        assertFalse(EstadoPedido.EN_PREPARACION.puedeTransicionarA(EstadoPedido.CANCELADO));

        assertTrue(EstadoPedido.LISTO.puedeTransicionarA(EstadoPedido.ENTREGADO));
        assertFalse(EstadoPedido.LISTO.puedeTransicionarA(EstadoPedido.EN_PREPARACION));

        assertFalse(EstadoPedido.ENTREGADO.puedeTransicionarA(EstadoPedido.RECIBIDO));
        assertFalse(EstadoPedido.RECIBIDO.puedeTransicionarA(null));
    }

    @Test
    @DisplayName("esCancelable - solo permite cancelación en estado RECIBIDO")
    void esCancelable_soloEnRecibido() {
        assertTrue(EstadoPedido.RECIBIDO.esCancelable());
        assertFalse(EstadoPedido.EN_PREPARACION.esCancelable());
        assertFalse(EstadoPedido.LISTO.esCancelable());
    }

    @Test
    @DisplayName("permiteModificacionDestilado - solo en RECIBIDO")
    void permiteModificacionDestilado_soloEnRecibido() {
        assertTrue(EstadoPedido.RECIBIDO.permiteModificacionDestilado());
        assertFalse(EstadoPedido.EN_PREPARACION.permiteModificacionDestilado());
    }
}