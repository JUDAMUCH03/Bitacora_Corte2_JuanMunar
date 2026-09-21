package co.edu.eci.dosw.restaurant.model.domain;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

public class Pedido {
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
