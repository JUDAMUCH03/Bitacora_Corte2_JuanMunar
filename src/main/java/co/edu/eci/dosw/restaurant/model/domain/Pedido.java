package co.edu.eci.dosw.restaurant.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

/**
 * Modelo de dominio para una comanda o pedido en Blue Velvet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    private Long id;
    private Long idMesa;

    @Builder.Default
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    private LocalDateTime timestamp;

    @Builder.Default
    private List<ItemPedido> items = new ArrayList<>();

    public boolean esCancelable() {
        return this.estado != null && this.estado.esCancelable();
    }

    public boolean permiteModificacionDestilado() {
        return this.estado != null && this.estado.permiteModificacionDestilado();
    }

    public boolean puedeTransicionarA(EstadoPedido siguiente) {
        return this.estado != null && this.estado.puedeTransicionarA(siguiente);
    }
}
