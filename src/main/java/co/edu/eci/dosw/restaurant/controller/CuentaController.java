package co.edu.eci.dosw.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.controller.docs.CuentaApi;
import co.edu.eci.dosw.restaurant.dto.response.CuentaResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.CuentaMapper;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.service.IMesaService;

@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
@Slf4j
public class CuentaController implements CuentaApi {

    private final IMesaService mesaService;
    private final CuentaMapper cuentaMapper;

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<CuentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("REST: GET /api/v1/cuentas/{}", id);
        Cuenta cuenta = mesaService.obtenerCuentaPorId(id);
        return ResponseEntity.ok(cuentaMapper.toResponse(cuenta));
    }

    @Override
    @PostMapping("/{id}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<CuentaResponseDTO> cerrarCuenta(@PathVariable Long id) {
        log.info("REST: POST /api/v1/cuentas/{}/cerrar", id);
        Cuenta cuenta = mesaService.cerrarCuenta(id);
        return ResponseEntity.ok(cuentaMapper.toResponse(cuenta));
    }
}
