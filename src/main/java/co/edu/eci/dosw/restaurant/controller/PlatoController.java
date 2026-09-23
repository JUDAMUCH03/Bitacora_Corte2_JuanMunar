package co.edu.eci.dosw.restaurant.controller;

import co.edu.eci.dosw.restaurant.controller.docs.PlatoApi;
import co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.PlatoMapperIn;
import co.edu.eci.dosw.restaurant.mapper.PlatoMapperOut;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.service.IPlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platos")
@RequiredArgsConstructor
@Slf4j
public class PlatoController implements PlatoApi {

    private final IPlatoService platoService;
    private final PlatoMapperIn mapperIn;
    private final PlatoMapperOut mapperOut;

    @Override
    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> listarTodos() {
        log.info("REST: GET /api/v1/platos");
        List<Plato> platos = platoService.obtenerTodos();
        return ResponseEntity.ok(mapperOut.toResponseList(platos));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("REST: GET /api/v1/platos/{}", id);
        Plato plato = platoService.obtenerPorId(id);
        return ResponseEntity.ok(mapperOut.toResponse(plato));
    }

    @Override
    @PostMapping
    public ResponseEntity<PlatoResponseDTO> crear(@RequestBody @Valid PlatoRequestDTO dto) {
        log.info("REST: POST /api/v1/platos - Nombre='{}'", dto.getNombre());
        Plato nuevoPlato = mapperIn.toDomain(dto);
        Plato creado = platoService.crear(nuevoPlato);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapperOut.toResponse(creado));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid PlatoRequestDTO dto) {
        log.info("REST: PUT /api/v1/platos/{}", id);
        Plato datosActualizados = mapperIn.toDomain(dto);
        Plato actualizado = platoService.actualizar(id, datosActualizados);
        return ResponseEntity.ok(mapperOut.toResponse(actualizado));
    }

    @Override
    @PatchMapping("/{id}/disponible")
    public ResponseEntity<PlatoResponseDTO> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam boolean disponible) {
        log.info("REST: PATCH /api/v1/platos/{}/disponible?disponible={}", id, disponible);
        Plato actualizado = platoService.cambiarDisponibilidad(id, disponible);
        return ResponseEntity.ok(mapperOut.toResponse(actualizado));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("REST: DELETE /api/v1/platos/{}", id);
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}