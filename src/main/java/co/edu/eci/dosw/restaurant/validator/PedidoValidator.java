package co.edu.eci.dosw.restaurant.validator;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.exception.EstadoInvalidoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.exception.ValidacionException;
import co.edu.eci.dosw.restaurant.model.domain.ItemPedido;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

@Component
@Slf4j
public class PedidoValidator implements IPedidoValidator {

    @Override
    public void validarCreacion(Pedido pedido) {
        if (pedido.getItems() == null || pedido.getItems().isEmpty()) {
            throw new ValidacionException("La comanda debe contener al menos un ítem");
        }

        for (ItemPedido item : pedido.getItems()) {
            if (item.getCantidadGarnishes() > 2) {
                log.warn("Validación fallida: Ítem excede máximo de 2 garnishes ({})", item.getCantidadGarnishes());
                throw new ValidacionException("Máximo 2 garnishes permitidos por ítem de bebida. Se recibieron: "
                        + item.getCantidadGarnishes());
            }
        }
    }

    @Override
    public void validarTransicionEstado(Pedido pedido, EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new ValidacionException("El nuevo estado no puede ser nulo");
        }

        if (nuevoEstado == EstadoPedido.CANCELADO && !pedido.esCancelable()) {
            log.warn("Intento de cancelación bloqueado: Pedido en estado {}", pedido.getEstado());
            throw new ReglaDeNegocioException("Solo se permite cancelar pedidos en estado RECIBIDO. Estado actual: "
                    + pedido.getEstado());
        }

        if (!pedido.puedeTransicionarA(nuevoEstado)) {
            log.warn("Transición de estado KDS inválida: {} -> {}", pedido.getEstado(), nuevoEstado);
            throw new EstadoInvalidoException("Transición inválida de estado en KDS: No se puede pasar de "
                    + pedido.getEstado() + " a " + nuevoEstado);
        }
    }

    @Override
    public void validarModificacionDestilado(Pedido pedido, ItemPedido item) {
        if (pedido.getEstado() != EstadoPedido.RECIBIDO) {
            log.warn("Mutación de destilado rechazada: Pedido en estado {}", pedido.getEstado());
            throw new ReglaDeNegocioException("La mutación de destilado solo es válida mientras el pedido esté en estado RECIBIDO (estado actual: "
                    + pedido.getEstado() + ")");
        }

        if (item.getSpiritModificationsCount() != null && item.getSpiritModificationsCount() >= 1) {
            log.warn("Mutación de destilado rechazada: Límite de modificaciones alcanzado ({})", item.getSpiritModificationsCount());
            throw new ReglaDeNegocioException("El destilado solo puede modificarse una única vez");
        }
    }
}
