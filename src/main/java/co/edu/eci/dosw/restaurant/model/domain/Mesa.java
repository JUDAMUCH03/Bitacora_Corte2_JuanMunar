package co.edu.eci.dosw.restaurant.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoMesa;

/**
 * Modelo de dominio para una mesa física en Blue Velvet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mesa {

    private Long id;
    private Integer numero;
    private Integer capacidad;

    @Builder.Default
    private EstadoMesa estado = EstadoMesa.DISPONIBLE;

    @Builder.Default
    private Boolean cuentaAbierta = false;

    public boolean tieneCuentaAbierta() {
        return Boolean.TRUE.equals(this.cuentaAbierta);
    }

    public void abrirCuenta() {
        this.cuentaAbierta = true;
        this.estado = EstadoMesa.OCUPADA;
    }

    public void liberar() {
        this.cuentaAbierta = false;
        this.estado = EstadoMesa.DISPONIBLE;
    }
}
