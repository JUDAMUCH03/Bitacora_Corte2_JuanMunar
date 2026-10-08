package co.edu.eci.dosw.restaurant.service;

import java.util.List;

import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

/**
 * Contrato de servicio para la gestión de comandas y el flujo KDS en Blue Velvet.
 */
public interface IPedidoService {

    Pedido crear(Pedido pedido);

    Pedido obtenerPorId(Long id);

    List<Pedido> obtenerTodos();

    List<Pedido> obtenerPorEstado(EstadoPedido estado);

    List<Pedido> obtenerPorMesa(Long idMesa);

    Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado);

    Pedido modificarDestilado(Long pedidoId, Long itemId, Long nuevoSpiritBrandId);
}
