package co.edu.eci.dosw.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import co.edu.eci.dosw.restaurant.service.IPedidoService;
import co.edu.eci.dosw.restaurant.validator.IPedidoValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;
    private final PedidoMapper pedidoMapper;
    private final IPedidoValidator pedidoValidator;
    private final IAuditoriaService auditoriaService;

    @Override
    @Transactional
    public Pedido crear(Pedido pedido) {
        log.info("Iniciando creación de comanda para mesa={}", pedido.getIdMesa());
        pedidoValidator.validarCreacion(pedido);

        for (ItemPedido item : pedido.getItems()) {
            PlatoEntity plato = platoRepository.findById(item.getIdPlato())
                    .orElseThrow(() -> {
                        log.warn("Plato no encontrado al registrar pedido: id={}", item.getIdPlato());
                        return new RecursoNoEncontradoException("Plato/Cóctel", item.getIdPlato());
                    });

            item.setPrecioCongelado(plato.getPrecio());
            item.setNombrePlato(plato.getNombre());
            if (item.getSpiritModificationsCount() == null) {
                item.setSpiritModificationsCount(0);
            }
            if (item.getGarnishes() == null) {
                item.setGarnishes(new ArrayList<>());
            }
        }

        pedido.setEstado(EstadoPedido.RECIBIDO);
        pedido.setTimestamp(LocalDateTime.now());

        PedidoEntity entity = pedidoMapper.toEntity(pedido);
        PedidoEntity guardado = pedidoRepository.save(entity);

        String usuario = obtenerUsuarioAutenticado();
        auditoriaService.registrarAuditoria(
                "CREACION_PEDIDO",
                guardado.getId().toString(),
                "Comanda creada para mesa " + guardado.getIdMesa() + " con " + guardado.getItems().size() + " ítems",
                usuario
        );

        log.info("Comanda creada exitosamente con ID={}", guardado.getId());
        return pedidoMapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        log.debug("Consultando comanda ID={}", id);
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toDomain)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado: ID={}", id);
                    return new RecursoNoEncontradoException("Pedido", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        log.debug("Consultando todas las comandas");
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        log.debug("Consultando comandas en estado={}", estado);
        return pedidoRepository.findByEstado(estado).stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorMesa(Long idMesa) {
        log.debug("Consultando comandas para mesa={}", idMesa);
        return pedidoRepository.findByIdMesa(idMesa).stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        log.info("Actualizando estado de comanda ID={} a {}", id, nuevoEstado);
        PedidoEntity entity = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado para actualización de estado: ID={}", id);
                    return new RecursoNoEncontradoException("Pedido", id);
                });

        Pedido domain = pedidoMapper.toDomain(entity);
        pedidoValidator.validarTransicionEstado(domain, nuevoEstado);

        EstadoPedido estadoAnterior = entity.getEstado();
        entity.setEstado(nuevoEstado);
        PedidoEntity guardado = pedidoRepository.save(entity);

        String usuario = obtenerUsuarioAutenticado();
        auditoriaService.registrarAuditoria(
                "CAMBIO_ESTADO_KDS",
                id.toString(),
                "Estado cambiado de " + estadoAnterior + " a " + nuevoEstado,
                usuario
        );

        log.info("Estado de comanda ID={} actualizado exitosamente a {}", id, nuevoEstado);
        return pedidoMapper.toDomain(guardado);
    }

    @Override
    @Transactional
    public Pedido modificarDestilado(Long pedidoId, Long itemId, Long nuevoSpiritBrandId) {
        log.info("Modificando destilado de ítem={} en comanda={}", itemId, pedidoId);
        PedidoEntity pedidoEntity = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado para modificación de destilado: ID={}", pedidoId);
                    return new RecursoNoEncontradoException("Pedido", pedidoId);
                });

        Pedido domain = pedidoMapper.toDomain(pedidoEntity);
        ItemPedido itemDomain = domain.getItems().stream()
                .filter(i -> itemId.equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Ítem {} no encontrado en comanda {}", itemId, pedidoId);
                    return new RecursoNoEncontradoException("ItemPedido", itemId);
                });

        pedidoValidator.validarModificacionDestilado(domain, itemDomain);

        ItemPedidoEntity itemEntity = pedidoEntity.getItems().stream()
                .filter(i -> itemId.equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("ItemPedido", itemId));

        itemEntity.setSpiritBrandId(nuevoSpiritBrandId);
        itemEntity.setSpiritModificationsCount(
                (itemEntity.getSpiritModificationsCount() == null ? 0 : itemEntity.getSpiritModificationsCount()) + 1
        );

        PedidoEntity guardado = pedidoRepository.save(pedidoEntity);

        String usuario = obtenerUsuarioAutenticado();
        auditoriaService.registrarAuditoria(
                "MODIFICACION_DESTILADO",
                pedidoId.toString(),
                "Destilado modificado en ítem " + itemId + " a marca " + nuevoSpiritBrandId,
                usuario
        );

        log.info("Destilado de ítem={} en comanda={} modificado exitosamente", itemId, pedidoId);
        return pedidoMapper.toDomain(guardado);
    }

    private String obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
