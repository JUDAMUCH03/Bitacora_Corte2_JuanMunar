package co.edu.eci.dosw.restaurant.controller.docs;

import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Platos", description = "Operaciones relacionadas con platos")
public interface PlatoApi {

    @Operation(summary = "Listar platos", description = "Retorna la lista de platos disponibles en el restaurante")
    @ApiResponse(responseCode = "200", description = "Lista de platos retornada exitosamente")
    @GetMapping
    ResponseEntity<List<PlatoResponseDTO>> listar();
}
