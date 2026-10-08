package co.edu.eci.dosw.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.controller.docs.AuditoriaApi;
import co.edu.eci.dosw.restaurant.dto.response.EventoAuditoriaResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.AuditoriaMapper;
import co.edu.eci.dosw.restaurant.persistence.document.EventoAuditoria;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;

@RestController
@RequestMapping("/api/v1/auditoria")
@RequiredArgsConstructor
@Slf4j
public class AuditoriaController implements AuditoriaApi {

    private final IAuditoriaService auditoriaService;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventoAuditoriaResponseDTO>> listarUltimosEventos() {
        log.info("REST: GET /api/v1/auditoria (Solo ADMIN)");
        List<EventoAuditoria> eventos = auditoriaService.listarUltimosEventos();
        return ResponseEntity.ok(eventos.stream().map(auditoriaMapper::toResponse).toList());
    }
}
