package co.edu.eci.dosw.restaurant.validator;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;

@Component
@Slf4j
public class MesaValidator implements IMesaValidator {

    @Override
    public void validarAperturaCuenta(Mesa mesa) {
        if (mesa.tieneCuentaAbierta()) {
            log.warn("Conflicto al abrir cuenta: Mesa número {} ya tiene cuenta activa", mesa.getNumero());
            throw new ConflictoException("Una mesa física abierta puede tener únicamente una (1) cuenta activa en estado ABIERTA. La mesa número "
                    + mesa.getNumero() + " ya tiene una cuenta activa.");
        }
    }

    @Override
    public void validarCierreCuenta(Cuenta cuenta) {
        if (cuenta.getEstado() != EstadoCuenta.ABIERTA) {
            log.warn("Cierre de cuenta rechazado: Cuenta id={} en estado {}", cuenta.getId(), cuenta.getEstado());
            throw new ReglaDeNegocioException("La cuenta con id=" + cuenta.getId()
                    + " no se puede liquidar porque no está en estado ABIERTA (estado actual: " + cuenta.getEstado() + ")");
        }
    }
}
