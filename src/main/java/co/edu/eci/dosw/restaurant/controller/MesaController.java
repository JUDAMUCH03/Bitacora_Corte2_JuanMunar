package co.edu.eci.dosw.restaurant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.controller.docs.MesaApi;
import co.edu.eci.dosw.restaurant.dto.response.CuentaResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.MesaResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.CuentaMapper;
import co.edu.eci.dosw.restaurant.mapper.MesaMapper;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.service.IMesaService;

@RestController
@RequestMapping("/api/v1/mesas")
@RequiredArgsConstructor
@Slf4j
public class MesaController implements MesaApi {

    private final IMesaService mesaService;
    private final MesaMapper mesaMapper;
    private final CuentaMapper cuentaMapper;

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'CHEF', 'BARTENDER')")
    public ResponseEntity<List<MesaResponseDTO>> listarTodas() {
        log.info("REST: GET /api/v1/mesas");
        List<Mesa> mesas = mesaService.obtenerTodas();
        return ResponseEntity.ok(mesas.stream().map(mesaMapper::toResponse).toList());
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'CHEF', 'BARTENDER')")
    public ResponseEntity<MesaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("REST: GET /api/v1/mesas/{}", id);
        Mesa mesa = mesaService.obtenerPorId(id);
        return ResponseEntity.ok(mesaMapper.toResponse(mesa));
    }

    @Override
    @PostMapping("/{id}/abrir-cuenta")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<CuentaResponseDTO> abrirCuenta(@PathVariable Long id) {
        log.info("REST: POST /api/v1/mesas/{}/abrir-cuenta", id);
        Cuenta cuenta = mesaService.abrirCuenta(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaMapper.toResponse(cuenta));
    }
}
