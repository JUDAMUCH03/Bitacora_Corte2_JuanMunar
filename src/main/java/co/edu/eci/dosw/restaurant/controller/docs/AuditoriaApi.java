package co.edu.eci.dosw.restaurant.controller.docs;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import co.edu.eci.dosw.restaurant.dto.response.EventoAuditoriaResponseDTO;

@Tag(name = "Auditoría", description = "Trazabilidad NoSQL (MongoDB) de eventos de comanda, KDS y apertura/cierre de cuentas (Exclusivo Administrador)")
public interface AuditoriaApi {

    @Operation(summary = "Consultar los últimos 50 eventos registrados en la bitácora de auditoría MongoDB")
    @ApiResponse(responseCode = "200", description = "Listado de eventos de auditoría obtenido")
    ResponseEntity<List<EventoAuditoriaResponseDTO>> listarUltimosEventos();
}
