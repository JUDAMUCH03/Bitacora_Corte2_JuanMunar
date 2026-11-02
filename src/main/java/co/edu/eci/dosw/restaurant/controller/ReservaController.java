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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.dto.request.ActualizarEstadoReservaDTO;
import co.edu.eci.dosw.restaurant.dto.request.ReservaRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ReservaResponseDTO;
import co.edu.eci.dosw.restaurant.service.IReservaService;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reservas", description = "Reservas de mesa de Blue Velvet")
public class ReservaController {

    private final IReservaService reservaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF', 'BARTENDER', 'MESERO')")
    @Operation(summary = "Listar reservas ordenadas por fecha y hora descendente (personal)")
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        log.info("REST: GET /api/v1/reservas");
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Crear una reserva de mesa (acceso público para comensales)")
    public ResponseEntity<ReservaResponseDTO> crear(@RequestBody @Valid ReservaRequestDTO dto) {
        log.info("REST: POST /api/v1/reservas - fecha={} hora={}", dto.getFecha(), dto.getHora());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(dto));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF', 'BARTENDER', 'MESERO')")
    @Operation(summary = "Cambiar el estado de una reserva (personal)")
    public ResponseEntity<ReservaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarEstadoReservaDTO dto) {
        log.info("REST: PATCH /api/v1/reservas/{}/estado -> {}", id, dto.getEstado());
        return ResponseEntity.ok(reservaService.cambiarEstado(id, dto.getEstado()));
    }
}
