package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.model.domain.ItemPedido;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

public interface IPedidoValidator {

    void validarCreacion(Pedido pedido);

    void validarTransicionEstado(Pedido pedido, EstadoPedido nuevoEstado);

    void validarModificacionDestilado(Pedido pedido, ItemPedido item);
}
