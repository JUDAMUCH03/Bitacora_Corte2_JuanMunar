package co.edu.eci.dosw.restaurant.controller;

import co.edu.eci.dosw.restaurant.controller.docs.MenuApi;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.out.PlatoMapper;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.service.IPlatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController implements MenuApi {

    private final IPlatoService platoService;
    private final PlatoMapper mapperOut;

    @Override
    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> verCarta() {
        log.info("REST: GET /api/v1/menu - Consulta de carta disponible");
        List<Plato> disponibles = platoService.obtenerDisponibles();
        return ResponseEntity.ok(mapperOut.toResponseList(disponibles));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> verDetalle(@PathVariable Long id) {
        log.info("REST: GET /api/v1/menu/{} - Detalle de producto", id);
        Plato plato = platoService.obtenerPorId(id);
        if (!plato.estaDisponible()) {
            throw new RecursoNoEncontradoException(
                    String.format("El cóctel o plato con id=%d se encuentra agotado en barra", id)
            );
        }
        return ResponseEntity.ok(mapperOut.toResponse(plato));
    }

    @Override
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<PlatoResponseDTO>> porCategoria(@PathVariable String categoria) {
        log.info("REST: GET /api/v1/menu/categoria/{} - Filtro por categoría", categoria);
        List<Plato> filtrados = platoService.obtenerPorCategoria(categoria).stream()
                .filter(Plato::estaDisponible)
                .toList();
        return ResponseEntity.ok(mapperOut.toResponseList(filtrados));
    }
}