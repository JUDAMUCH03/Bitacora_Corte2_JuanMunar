package co.edu.eci.dosw.restaurant.controller.docs;

import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Menú", description = "Consulta pública de la carta digital (Vista Comensal)")
public interface MenuApi {

    @Operation(summary = "Consultar carta disponible en tiempo real")
    @ApiResponse(responseCode = "200", description = "Lista de cócteles y productos disponibles")
    ResponseEntity<List<PlatoResponseDTO>> verCarta();

    @Operation(summary = "Consultar detalle de un trago disponible")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle del cóctel"),
        @ApiResponse(responseCode = "404", description = "Producto no disponible o inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PlatoResponseDTO> verDetalle(Long id);

    @Operation(summary = "Filtrar carta por categoría (ej. MOCKTAIL, COCTEL_AUTOR)")
    @ApiResponse(responseCode = "200", description = "Productos disponibles de la categoría seleccionada")
    ResponseEntity<List<PlatoResponseDTO>> porCategoria(String categoria);
}