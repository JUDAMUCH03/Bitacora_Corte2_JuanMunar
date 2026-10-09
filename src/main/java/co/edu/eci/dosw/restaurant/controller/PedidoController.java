package co.edu.eci.dosw.restaurant.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.controller.docs.PedidoApi;
import co.edu.eci.dosw.restaurant.dto.request.ActualizarEstadoPedidoDTO;
import co.edu.eci.dosw.restaurant.dto.request.ModificarDestiladoDTO;
import co.edu.eci.dosw.restaurant.dto.request.PedidoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.PedidoResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.PedidoMapper;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;
import co.edu.eci.dosw.restaurant.service.IPedidoService;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoController implements PedidoApi {

    private final IPedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<PedidoResponseDTO> crear(@RequestBody @Valid PedidoRequestDTO dto) {
        log.info("REST: POST /api/v1/pedidos para mesa={}", dto.getIdMesa());
        Pedido domain = pedidoMapper.toDomain(dto);
        Pedido creado = pedidoService.crear(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(creado));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'CHEF', 'BARTENDER')")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("REST: GET /api/v1/pedidos/{}", id);
        Pedido pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'CHEF', 'BARTENDER')")
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) Long idMesa) {
        log.info("REST: GET /api/v1/pedidos?estado={}&idMesa={}", estado, idMesa);
        List<Pedido> pedidos;
        if (estado != null) {
            pedidos = pedidoService.obtenerPorEstado(estado);
        } else if (idMesa != null) {
            pedidos = pedidoService.obtenerPorMesa(idMesa);
        } else {
            pedidos = pedidoService.obtenerTodos();
        }
        return ResponseEntity.ok(pedidos.stream().map(pedidoMapper::toResponse).toList());
    }

    @Override
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'CHEF', 'BARTENDER')")
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarEstadoPedidoDTO dto) {
        log.info("REST: PATCH /api/v1/pedidos/{}/estado a {}", id, dto.getNuevoEstado());
        Pedido actualizado = pedidoService.actualizarEstado(id, dto.getNuevoEstado());
        return ResponseEntity.ok(pedidoMapper.toResponse(actualizado));
    }

    @Override
    @PatchMapping("/{id}/items/{itemId}/destilado")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'BARTENDER')")
    public ResponseEntity<PedidoResponseDTO> modificarDestilado(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestBody @Valid ModificarDestiladoDTO dto) {
        log.info("REST: PATCH /api/v1/pedidos/{}/items/{}/destilado a brandId={}", id, itemId, dto.getNuevoSpiritBrandId());
        Pedido actualizado = pedidoService.modificarDestilado(id, itemId, dto.getNuevoSpiritBrandId());
        return ResponseEntity.ok(pedidoMapper.toResponse(actualizado));
    }
}
