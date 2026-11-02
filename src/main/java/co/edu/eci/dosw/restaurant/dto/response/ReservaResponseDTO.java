package co.edu.eci.dosw.restaurant.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponseDTO {

    private Long id;
    private String nombreCliente;
    private String emailCliente;
    private String telefono;
    private LocalDate fecha;
    private String hora;
    private Integer numeroPersonas;
    private Integer mesaNumero;
    private String zona;
    private EstadoReserva estado;
    private String notas;
    private LocalDateTime creadoEn;
}
