package co.edu.eci.dosw.restaurant.model.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio para un ítem dentro de una comanda en Blue Velvet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    private Long id;
    private Long idPlato;
    private String nombrePlato;
    private Double precioCongelado;
    private Integer cantidad;
    private Long spiritBrandId;

    @Builder.Default
    private Integer spiritModificationsCount = 0;

    @Builder.Default
    private List<String> garnishes = new ArrayList<>();

    public int getCantidadGarnishes() {
        return this.garnishes != null ? this.garnishes.size() : 0;
    }

    public boolean puedeModificarDestilado() {
        return this.spiritModificationsCount != null && this.spiritModificationsCount < 1;
    }

    public void modificarDestilado(Long nuevoSpiritBrandId) {
        this.spiritBrandId = nuevoSpiritBrandId;
        this.spiritModificationsCount = (this.spiritModificationsCount == null ? 0 : this.spiritModificationsCount) + 1;
    }
}
