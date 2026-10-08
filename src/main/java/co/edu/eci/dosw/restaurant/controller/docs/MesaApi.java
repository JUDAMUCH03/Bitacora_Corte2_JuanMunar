package co.edu.eci.dosw.restaurant.controller.docs;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import co.edu.eci.dosw.restaurant.dto.response.CuentaResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.MesaResponseDTO;

@Tag(name = "Mesas", description = "Gestión de mesas físicas y apertura de cuentas en sala")
public interface MesaApi {

    @Operation(summary = "Listar todas las mesas del restaurante")
    @ApiResponse(responseCode = "200", description = "Listado completo de mesas")
    ResponseEntity<List<MesaResponseDTO>> listarTodas();

    @Operation(summary = "Consultar detalle de una mesa por identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mesa encontrada"),
        @ApiResponse(responseCode = "404", description = "Mesa inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<MesaResponseDTO> obtenerPorId(Long id);

    @Operation(summary = "Abrir cuenta sobre una mesa física")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cuenta abierta con éxito"),
        @ApiResponse(responseCode = "404", description = "Mesa inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "La mesa ya tiene una cuenta activa en estado ABIERTA",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<CuentaResponseDTO> abrirCuenta(Long id);
}
