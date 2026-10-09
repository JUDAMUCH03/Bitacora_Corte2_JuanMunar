package co.edu.eci.dosw.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.CuentaMapper;
import co.edu.eci.dosw.restaurant.mapper.MesaMapper;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoMesa;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;
import co.edu.eci.dosw.restaurant.persistence.entity.CuentaEntity;
import co.edu.eci.dosw.restaurant.persistence.entity.MesaEntity;
import co.edu.eci.dosw.restaurant.persistence.entity.PedidoEntity;
import co.edu.eci.dosw.restaurant.repository.CuentaRepository;
import co.edu.eci.dosw.restaurant.repository.MesaRepository;
import co.edu.eci.dosw.restaurant.repository.PedidoRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import co.edu.eci.dosw.restaurant.service.IMesaService;
import co.edu.eci.dosw.restaurant.validator.IMesaValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class MesaServiceImpl implements IMesaService {

    private final MesaRepository mesaRepository;
    private final CuentaRepository cuentaRepository;
    private final PedidoRepository pedidoRepository;
    private final MesaMapper mesaMapper;
    private final CuentaMapper cuentaMapper;
    private final IMesaValidator mesaValidator;
    private final IAuditoriaService auditoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<Mesa> obtenerTodas() {
        log.debug("Consultando todas las mesas");
        return mesaRepository.findAll().stream()
                .map(mesaMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Mesa obtenerPorId(Long id) {
        log.debug("Consultando mesa con ID={}", id);
        return mesaRepository.findById(id)
                .map(mesaMapper::toDomain)
                .orElseThrow(() -> {
                    log.warn("Mesa no encontrada: ID={}", id);
                    return new RecursoNoEncontradoException("Mesa", id);
                });
    }

    @Override
    @Transactional
    public Mesa crear(Mesa mesa) {
        log.info("Creando mesa número {}", mesa.getNumero());
        if (mesa.getCuentaAbierta() == null) {
            mesa.setCuentaAbierta(false);
        }
        if (mesa.getEstado() == null) {
            mesa.setEstado(EstadoMesa.DISPONIBLE);
        }
        MesaEntity entity = mesaMapper.toEntity(mesa);
        MesaEntity guardada = mesaRepository.save(entity);
        return mesaMapper.toDomain(guardada);
    }

    @Override
    @Transactional
    public Cuenta abrirCuenta(Long idMesa) {
        log.info("Iniciando apertura de cuenta para mesa ID={}", idMesa);
        MesaEntity mesaEntity = mesaRepository.findById(idMesa)
                .orElseThrow(() -> {
                    log.warn("Mesa no encontrada: ID={}", idMesa);
                    return new RecursoNoEncontradoException("Mesa", idMesa);
                });

        Mesa mesaDomain = mesaMapper.toDomain(mesaEntity);
        mesaValidator.validarAperturaCuenta(mesaDomain);

        if (cuentaRepository.existsByIdMesaAndEstado(idMesa, EstadoCuenta.ABIERTA)) {
            log.warn("Conflicto: Ya existe cuenta ABIERTA para mesa ID={}", idMesa);
            throw new ConflictoException("Una mesa física abierta puede tener únicamente una (1) cuenta activa en estado ABIERTA. La mesa número "
                    + mesaDomain.getNumero() + " ya tiene una cuenta activa.");
        }

        mesaEntity.setCuentaAbierta(true);
        mesaEntity.setEstado(EstadoMesa.OCUPADA);
        mesaRepository.save(mesaEntity);

        CuentaEntity cuentaEntity = CuentaEntity.builder()
                .idMesa(idMesa)
                .total(0.0)
                .estado(EstadoCuenta.ABIERTA)
                .fechaApertura(LocalDateTime.now())
                .build();

        CuentaEntity guardada = cuentaRepository.save(cuentaEntity);

        String usuario = obtenerUsuarioAutenticado();
        auditoriaService.registrarAuditoria(
                "APERTURA_CUENTA",
                guardada.getId().toString(),
                "Cuenta abierta para mesa " + mesaEntity.getNumero(),
                usuario
        );

        log.info("Cuenta ID={} abierta exitosamente para mesa={}", guardada.getId(), idMesa);
        return cuentaMapper.toDomain(guardada);
    }

    @Override
    @Transactional
    public Cuenta cerrarCuenta(Long idCuenta) {
        log.info("Iniciando cierre de cuenta ID={}", idCuenta);
        CuentaEntity cuentaEntity = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada: ID={}", idCuenta);
                    return new RecursoNoEncontradoException("Cuenta", idCuenta);
                });

        Cuenta cuentaDomain = cuentaMapper.toDomain(cuentaEntity);
        mesaValidator.validarCierreCuenta(cuentaDomain);

        List<PedidoEntity> pedidos = pedidoRepository.findByIdMesa(cuentaEntity.getIdMesa());
        double totalCalculado = pedidos.stream()
                .filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                .flatMap(p -> p.getItems().stream())
                .mapToDouble(i -> (i.getPrecioCongelado() != null ? i.getPrecioCongelado() : 0.0)
                        * (i.getCantidad() != null ? i.getCantidad() : 1))
                .sum();

        if (totalCalculado > 0.0 || cuentaEntity.getTotal() == null || cuentaEntity.getTotal() == 0.0) {
            cuentaEntity.setTotal(totalCalculado);
        }

        cuentaEntity.setEstado(EstadoCuenta.PAGADA);
        CuentaEntity guardada = cuentaRepository.save(cuentaEntity);

        mesaRepository.findById(cuentaEntity.getIdMesa()).ifPresent(mesa -> {
            mesa.setCuentaAbierta(false);
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesaRepository.save(mesa);
        });

        String usuario = obtenerUsuarioAutenticado();
        auditoriaService.registrarAuditoria(
                "CIERRE_CUENTA",
                guardada.getId().toString(),
                "Cuenta liquidada con total $" + guardada.getTotal() + " para mesa " + cuentaEntity.getIdMesa(),
                usuario
        );

        log.info("Cuenta ID={} cerrada exitosamente", guardada.getId());
        return cuentaMapper.toDomain(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta obtenerCuentaPorId(Long idCuenta) {
        log.debug("Consultando cuenta ID={}", idCuenta);
        return cuentaRepository.findById(idCuenta)
                .map(cuentaMapper::toDomain)
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada: ID={}", idCuenta);
                    return new RecursoNoEncontradoException("Cuenta", idCuenta);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasPorMesa(Long idMesa) {
        log.debug("Consultando cuentas para mesa={}", idMesa);
        return cuentaRepository.findByIdMesa(idMesa).stream()
                .map(cuentaMapper::toDomain)
                .toList();
    }

    private String obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
