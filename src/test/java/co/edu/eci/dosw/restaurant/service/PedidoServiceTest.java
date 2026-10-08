package co.edu.eci.dosw.restaurant.service;

import java.util.ArrayList;
import java.util.List;
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

import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.PedidoMapper;
import co.edu.eci.dosw.restaurant.model.domain.ItemPedido;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;
import co.edu.eci.dosw.restaurant.persistence.entity.ItemPedidoEntity;
import co.edu.eci.dosw.restaurant.persistence.entity.PedidoEntity;
import co.edu.eci.dosw.restaurant.persistence.entity.PlatoEntity;
import co.edu.eci.dosw.restaurant.repository.PedidoRepository;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;
import co.edu.eci.dosw.restaurant.service.impl.PedidoServiceImpl;
import co.edu.eci.dosw.restaurant.validator.IPedidoValidator;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PlatoRepository platoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private IPedidoValidator pedidoValidator;

    @Mock
    private IAuditoriaService auditoriaService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    @DisplayName("crear - Congela precio de la carta, valida comanda, persiste y registra auditoría")
    void crear_flujoExitoso_congelaPrecioYRegistraAuditoria() {
        PlatoEntity plato = PlatoEntity.builder()
                .id(1L)
                .nombre("Negroni Clásico")
                .precio(38000.0)
                .build();

        ItemPedido item = ItemPedido.builder()
                .idPlato(1L)
                .cantidad(2)
                .garnishes(new ArrayList<>(List.of("Piel de naranja")))
                .build();

        Pedido pedido = Pedido.builder()
                .idMesa(5L)
                .items(new ArrayList<>(List.of(item)))
                .build();

        PedidoEntity entityGuardada = PedidoEntity.builder()
                .id(100L)
                .idMesa(5L)
                .estado(EstadoPedido.RECIBIDO)
                .items(new ArrayList<>())
                .build();

        when(platoRepository.findById(1L)).thenReturn(Optional.of(plato));
        when(pedidoMapper.toEntity(any(Pedido.class))).thenReturn(entityGuardada);
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(entityGuardada);
        when(pedidoMapper.toDomain(entityGuardada)).thenReturn(pedido);

        Pedido resultado = pedidoService.crear(pedido);

        assertNotNull(resultado);
        assertEquals(38000.0, item.getPrecioCongelado());
        assertEquals("Negroni Clásico", item.getNombrePlato());
        assertEquals(EstadoPedido.RECIBIDO, pedido.getEstado());

        verify(pedidoValidator).validarCreacion(pedido);
        verify(pedidoRepository).save(any(PedidoEntity.class));
        verify(auditoriaService).registrarAuditoria(eq("CREACION_PEDIDO"), eq("100"), anyString(), anyString());
    }

    @Test
    @DisplayName("crear - Plato inexistente lanza RecursoNoEncontradoException")
    void crear_platoInexistente_lanzaRecursoNoEncontradoException() {
        ItemPedido item = ItemPedido.builder().idPlato(999L).cantidad(1).build();
        Pedido pedido = Pedido.builder().idMesa(1L).items(List.of(item)).build();

        when(platoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> pedidoService.crear(pedido));
    }

    @Test
    @DisplayName("actualizarEstado - Transición válida actualiza estado y audita en MongoDB")
    void actualizarEstado_transicionValida_actualizaYAudita() {
        PedidoEntity entity = PedidoEntity.builder()
                .id(10L)
                .idMesa(2L)
                .estado(EstadoPedido.RECIBIDO)
                .build();

        Pedido domain = Pedido.builder()
                .id(10L)
                .idMesa(2L)
                .estado(EstadoPedido.RECIBIDO)
                .build();

        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(pedidoMapper.toDomain(entity)).thenReturn(domain);
        when(pedidoRepository.save(entity)).thenReturn(entity);
        when(pedidoMapper.toDomain(entity)).thenReturn(domain);

        Pedido resultado = pedidoService.actualizarEstado(10L, EstadoPedido.EN_PREPARACION);

        assertNotNull(resultado);
        assertEquals(EstadoPedido.EN_PREPARACION, entity.getEstado());
        verify(pedidoValidator).validarTransicionEstado(domain, EstadoPedido.EN_PREPARACION);
        verify(pedidoRepository).save(entity);
        verify(auditoriaService).registrarAuditoria(eq("CAMBIO_ESTADO_KDS"), eq("10"), anyString(), anyString());
    }

    @Test
    @DisplayName("actualizarEstado - Pedido inexistente lanza RecursoNoEncontradoException")
    void actualizarEstado_pedidoInexistente_lanzaRecursoNoEncontradoException() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () ->
                pedidoService.actualizarEstado(999L, EstadoPedido.EN_PREPARACION));
    }

    @Test
    @DisplayName("modificarDestilado - Modificación exitosa incrementa contador y audita")
    void modificarDestilado_exitoso_incrementaContadorYAudita() {
        ItemPedidoEntity itemEntity = ItemPedidoEntity.builder()
                .id(1L)
                .idPlato(10L)
                .nombrePlato("Dry Martini")
                .precioCongelado(45000.0)
                .cantidad(1)
                .spiritBrandId(50L)
                .spiritModificationsCount(0)
                .build();

        PedidoEntity pedidoEntity = PedidoEntity.builder()
                .id(20L)
                .idMesa(3L)
                .estado(EstadoPedido.RECIBIDO)
                .items(new ArrayList<>(List.of(itemEntity)))
                .build();

        ItemPedido itemDomain = ItemPedido.builder()
                .id(1L)
                .idPlato(10L)
                .spiritBrandId(50L)
                .spiritModificationsCount(0)
                .build();

        Pedido pedidoDomain = Pedido.builder()
                .id(20L)
                .idMesa(3L)
                .estado(EstadoPedido.RECIBIDO)
                .items(new ArrayList<>(List.of(itemDomain)))
                .build();

        when(pedidoRepository.findById(20L)).thenReturn(Optional.of(pedidoEntity));
        when(pedidoMapper.toDomain(pedidoEntity)).thenReturn(pedidoDomain);
        when(pedidoRepository.save(pedidoEntity)).thenReturn(pedidoEntity);
        when(pedidoMapper.toDomain(pedidoEntity)).thenReturn(pedidoDomain);

        Pedido resultado = pedidoService.modificarDestilado(20L, 1L, 99L);

        assertNotNull(resultado);
        assertEquals(99L, itemEntity.getSpiritBrandId());
        assertEquals(1, itemEntity.getSpiritModificationsCount());

        verify(pedidoValidator).validarModificacionDestilado(pedidoDomain, itemDomain);
        verify(pedidoRepository).save(pedidoEntity);
        verify(auditoriaService).registrarAuditoria(eq("MODIFICACION_DESTILADO"), eq("20"), anyString(), anyString());
    }
}
