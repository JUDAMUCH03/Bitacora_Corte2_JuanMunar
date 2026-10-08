package co.edu.eci.dosw.restaurant.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.CuentaMapper;
import co.edu.eci.dosw.restaurant.mapper.MesaMapper;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoMesa;
import co.edu.eci.dosw.restaurant.persistence.entity.CuentaEntity;
import co.edu.eci.dosw.restaurant.persistence.entity.MesaEntity;
import co.edu.eci.dosw.restaurant.repository.CuentaRepository;
import co.edu.eci.dosw.restaurant.repository.MesaRepository;
import co.edu.eci.dosw.restaurant.repository.PedidoRepository;
import co.edu.eci.dosw.restaurant.service.impl.MesaServiceImpl;
import co.edu.eci.dosw.restaurant.validator.IMesaValidator;

@ExtendWith(MockitoExtension.class)
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MesaMapper mesaMapper;

    @Mock
    private CuentaMapper cuentaMapper;

    @Mock
    private IMesaValidator mesaValidator;

    @Mock
    private IAuditoriaService auditoriaService;

    @InjectMocks
    private MesaServiceImpl mesaService;

    @Test
    @DisplayName("abrirCuenta - Abre cuenta para mesa disponible, actualiza estado a OCUPADA y audita")
    void abrirCuenta_exitoso_actualizaMesaYAudita() {
        MesaEntity mesaEntity = MesaEntity.builder()
                .id(1L)
                .numero(4)
                .capacidad(4)
                .estado(EstadoMesa.DISPONIBLE)
                .cuentaAbierta(false)
                .build();

        Mesa mesaDomain = Mesa.builder()
                .id(1L)
                .numero(4)
                .capacidad(4)
                .estado(EstadoMesa.DISPONIBLE)
                .cuentaAbierta(false)
                .build();

        CuentaEntity cuentaEntity = CuentaEntity.builder()
                .id(50L)
                .idMesa(1L)
                .total(0.0)
                .estado(EstadoCuenta.ABIERTA)
                .fechaApertura(LocalDateTime.now())
                .build();

        Cuenta cuentaDomain = Cuenta.builder()
                .id(50L)
                .idMesa(1L)
                .total(0.0)
                .estado(EstadoCuenta.ABIERTA)
                .build();

        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));
        when(mesaMapper.toDomain(mesaEntity)).thenReturn(mesaDomain);
        when(cuentaRepository.existsByIdMesaAndEstado(1L, EstadoCuenta.ABIERTA)).thenReturn(false);
        when(cuentaRepository.save(any(CuentaEntity.class))).thenReturn(cuentaEntity);
        when(cuentaMapper.toDomain(cuentaEntity)).thenReturn(cuentaDomain);

        Cuenta resultado = mesaService.abrirCuenta(1L);

        assertNotNull(resultado);
        assertEquals(EstadoCuenta.ABIERTA, resultado.getEstado());
        verify(mesaValidator).validarAperturaCuenta(mesaDomain);
        verify(mesaRepository).save(mesaEntity);
        verify(auditoriaService).registrarAuditoria(eq("APERTURA_CUENTA"), eq("50"), anyString(), anyString());
    }

    @Test
    @DisplayName("abrirCuenta - Cuenta activa ya existente lanza ConflictoException (HTTP 409)")
    void abrirCuenta_yaExisteCuentaAbierta_lanzaConflictoException() {
        MesaEntity mesaEntity = MesaEntity.builder()
                .id(1L)
                .numero(4)
                .capacidad(4)
                .estado(EstadoMesa.DISPONIBLE)
                .cuentaAbierta(false)
                .build();

        Mesa mesaDomain = Mesa.builder()
                .id(1L)
                .numero(4)
                .cuentaAbierta(false)
                .build();

        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));
        when(mesaMapper.toDomain(mesaEntity)).thenReturn(mesaDomain);
        when(cuentaRepository.existsByIdMesaAndEstado(1L, EstadoCuenta.ABIERTA)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> mesaService.abrirCuenta(1L));
    }

    @Test
    @DisplayName("cerrarCuenta - Liquida cuenta, libera mesa a DISPONIBLE y audita")
    void cerrarCuenta_exitoso_liberaMesaYAudita() {
        CuentaEntity cuentaEntity = CuentaEntity.builder()
                .id(50L)
                .idMesa(1L)
                .total(120000.0)
                .estado(EstadoCuenta.ABIERTA)
                .build();

        Cuenta cuentaDomain = Cuenta.builder()
                .id(50L)
                .idMesa(1L)
                .total(120000.0)
                .estado(EstadoCuenta.ABIERTA)
                .build();

        MesaEntity mesaEntity = MesaEntity.builder()
                .id(1L)
                .numero(4)
                .estado(EstadoMesa.OCUPADA)
                .cuentaAbierta(true)
                .build();

        when(cuentaRepository.findById(50L)).thenReturn(Optional.of(cuentaEntity));
        when(cuentaMapper.toDomain(cuentaEntity)).thenReturn(cuentaDomain);
        when(cuentaRepository.save(cuentaEntity)).thenReturn(cuentaEntity);
        when(cuentaMapper.toDomain(cuentaEntity)).thenReturn(cuentaDomain);
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));

        Cuenta resultado = mesaService.cerrarCuenta(50L);

        assertNotNull(resultado);
        assertEquals(EstadoCuenta.PAGADA, cuentaEntity.getEstado());
        assertEquals(EstadoMesa.DISPONIBLE, mesaEntity.getEstado());
        assertEquals(false, mesaEntity.getCuentaAbierta());

        verify(mesaValidator).validarCierreCuenta(cuentaDomain);
        verify(mesaRepository).save(mesaEntity);
        verify(auditoriaService).registrarAuditoria(eq("CIERRE_CUENTA"), eq("50"), anyString(), anyString());
    }

    @Test
    @DisplayName("cerrarCuenta - Cuenta inexistente lanza RecursoNoEncontradoException")
    void cerrarCuenta_noExiste_lanzaRecursoNoEncontradoException() {
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> mesaService.cerrarCuenta(999L));
    }
}
