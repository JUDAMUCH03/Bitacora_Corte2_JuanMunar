package co.edu.eci.dosw.restaurant.service;

import java.util.List;

import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;

/**
 * Contrato de servicio para la gestión de mesas físicas y apertura/cierre de cuentas.
 */
public interface IMesaService {

    List<Mesa> obtenerTodas();

    Mesa obtenerPorId(Long id);

    Mesa crear(Mesa mesa);

    Cuenta abrirCuenta(Long idMesa);

    Cuenta cerrarCuenta(Long idCuenta);

    Cuenta obtenerCuentaPorId(Long idCuenta);

    List<Cuenta> obtenerCuentasPorMesa(Long idMesa);
}
