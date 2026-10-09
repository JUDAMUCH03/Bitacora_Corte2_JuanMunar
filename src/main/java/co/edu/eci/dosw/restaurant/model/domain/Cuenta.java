package co.edu.eci.dosw.restaurant.model.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;

/**
 * Modelo de dominio para la cuenta de una mesa en Blue Velvet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {

    private Long id;
    private Long idMesa;

    @Builder.Default
    private Double total = 0.0;

    @Builder.Default
    private EstadoCuenta estado = EstadoCuenta.ABIERTA;

    private LocalDateTime fechaApertura;

    public boolean estaAbierta() {
        return this.estado == EstadoCuenta.ABIERTA;
    }

    public void pagar() {
        this.estado = EstadoCuenta.PAGADA;
    }
}
