package co.edu.eci.dosw.restaurant.service;

import java.util.List;

import co.edu.eci.dosw.restaurant.dto.request.ReservaRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ReservaResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;

/**
 * Contrato del servicio de reservas de mesa para Blue Velvet.
 */
public interface IReservaService {

    List<ReservaResponseDTO> listarTodas();

    ReservaResponseDTO crear(ReservaRequestDTO solicitud);

    ReservaResponseDTO cambiarEstado(Long id, EstadoReserva nuevoEstado);
}
