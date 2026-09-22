package co.edu.eci.dosw.restaurant.controller.docs;

import co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO;
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

@Tag(name = "Platos", description = "Administración completa de la carta y coctelería (Rol Admin)")
public interface PlatoApi {

    @Operation(summary = "Listar todos los cócteles y platos registrados")
    @ApiResponse(responseCode = "200", description = "Catálogo completo obtenido")
    ResponseEntity<List<PlatoResponseDTO>> listarTodos();

    @Operation(summary = "Obtener un trago o plato por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PlatoResponseDTO> obtenerPorId(Long id);

    @Operation(summary = "Crear un nuevo cóctel o producto en carta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Nombre de cóctel duplicado",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PlatoResponseDTO> crear(PlatoRequestDTO dto);

    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto inexistente"),
        @ApiResponse(responseCode = "409", description = "Nombre duplicado con otro producto")
    })
    ResponseEntity<PlatoResponseDTO> actualizar(Long id, PlatoRequestDTO dto);

    @Operation(summary = "Modificar disponibilidad en barra (Activar / Inhabilitar)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad modificada"),
        @ApiResponse(responseCode = "404", description = "Producto inexistente")
    })
    ResponseEntity<PlatoResponseDTO> cambiarDisponibilidad(Long id, boolean disponible);

    @Operation(summary = "Eliminar un producto de la carta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado (sin cuerpo)"),
        @ApiResponse(responseCode = "404", description = "Producto inexistente")
    })
    ResponseEntity<Void> eliminar(Long id);
}