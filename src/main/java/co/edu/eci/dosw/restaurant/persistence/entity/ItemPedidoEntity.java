package co.edu.eci.dosw.restaurant.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_plato", nullable = false)
    private Long idPlato;

    @Column(name = "nombre_plato", nullable = false)
    private String nombrePlato;

    @Column(name = "precio_congelado", nullable = false)
    private Double precioCongelado;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "spirit_brand_id")
    private Long spiritBrandId;

    @Builder.Default
    @Column(name = "spirit_modifications_count", nullable = false)
    private Integer spiritModificationsCount = 0;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "item_pedido_garnishes", joinColumns = @JoinColumn(name = "item_pedido_id"))
    @Column(name = "garnish")
    private List<String> garnishes = new ArrayList<>();
}
