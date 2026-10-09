package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;

public interface IMesaValidator {

    void validarAperturaCuenta(Mesa mesa);

    void validarCierreCuenta(Cuenta cuenta);
}
