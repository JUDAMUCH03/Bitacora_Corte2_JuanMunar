package co.edu.eci.dosw.restaurant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.eci.dosw.restaurant.dto.request.ReservaRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ReservaResponseDTO;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.exception.ValidacionException;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;
import co.edu.eci.dosw.restaurant.persistence.entity.ReservaEntity;
import co.edu.eci.dosw.restaurant.repository.ReservaRepository;
import co.edu.eci.dosw.restaurant.service.impl.ReservaServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private IAuditoriaService auditoriaService;

    @InjectMocks
    private ReservaServiceImpl service;

    private ReservaRequestDTO solicitudValida(LocalDate fecha) {
        return ReservaRequestDTO.builder()
                .nombreCliente("  Sofia Gómez ")
                .emailCliente("sofia@ejemplo.com")
                .telefono("3000000000")
                .fecha(fecha)
                .hora("20:30")
                .numeroPersonas(4)
                .zona("Salón Principal")
                .notas("Aniversario")
                .build();
    }

    private ReservaEntity entidadGuardada(Long id, EstadoReserva estado) {
        return ReservaEntity.builder()
                .id(id)
                .nombreCliente("Sofia Gómez")
                .emailCliente("sofia@ejemplo.com")
                .fecha(LocalDate.now().plusDays(2))
                .hora("20:30")
                .numeroPersonas(4)
                .estado(estado)
                .build();
    }

    @Test
    @DisplayName("crear persiste la reserva como CONFIRMADA y devuelve el ID generado")
    void crearPersisteReservaConfirmada() {
        when(reservaRepository.save(any(ReservaEntity.class)))
                .thenAnswer(inv -> {
                    ReservaEntity e = inv.getArgument(0);
                    e.setId(77L);
                    return e;
                });

        ReservaResponseDTO resp = service.crear(solicitudValida(LocalDate.now().plusDays(2)));

        assertEquals(77L, resp.getId());
        assertEquals(EstadoReserva.CONFIRMADA, resp.getEstado());
        assertEquals("Sofia Gómez", resp.getNombreCliente());
        assertEquals(4, resp.getNumeroPersonas());
    }

    @Test
    @DisplayName("crear rechaza fechas en el pasado sin tocar la base de datos")
    void crearRechazaFechaPasada() {
        assertThrows(ValidacionException.class,
                () -> service.crear(solicitudValida(LocalDate.now().minusDays(1))));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("un fallo en la bitácora documental NO impide registrar la reserva")
    void crearNoDependeDeAuditoria() {
        when(reservaRepository.save(any(ReservaEntity.class)))
                .thenAnswer(inv -> {
                    ReservaEntity e = inv.getArgument(0);
                    e.setId(5L);
                    return e;
                });
        doThrow(new RuntimeException("mongo caído"))
                .when(auditoriaService).registrarEvento(any(EventoRestaurante.class));

        ReservaResponseDTO resp = assertDoesNotThrow(
                () -> service.crear(solicitudValida(LocalDate.now().plusDays(1))));

        assertEquals(5L, resp.getId());
    }

    @Test
    @DisplayName("listarTodas devuelve las reservas ordenadas por el repositorio")
    void listarTodas() {
        when(reservaRepository.findAllByOrderByFechaDescHoraDesc())
                .thenReturn(List.of(entidadGuardada(2L, EstadoReserva.CONFIRMADA),
                        entidadGuardada(1L, EstadoReserva.PENDIENTE)));

        List<ReservaResponseDTO> resultado = service.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals(2L, resultado.get(0).getId());
    }

    @Test
    @DisplayName("cambiarEstado actualiza y persiste el nuevo estado")
    void cambiarEstado() {
        ReservaEntity entidad = entidadGuardada(9L, EstadoReserva.CONFIRMADA);
        when(reservaRepository.findById(9L)).thenReturn(Optional.of(entidad));
        when(reservaRepository.save(entidad)).thenReturn(entidad);

        ReservaResponseDTO resp = service.cambiarEstado(9L, EstadoReserva.CANCELADA);

        assertEquals(EstadoReserva.CANCELADA, resp.getEstado());
        verify(reservaRepository).save(entidad);
    }

    @Test
    @DisplayName("cambiarEstado lanza 404 cuando la reserva no existe")
    void cambiarEstadoNoExiste() {
        when(reservaRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.cambiarEstado(404L, EstadoReserva.CANCELADA));
    }
}
