package co.edu.eci.dosw.restaurant.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import co.edu.eci.dosw.restaurant.dto.response.CuentaResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;

@Tag(name = "Cuentas", description = "Gestión y liquidación de cuentas de consumo")
public interface CuentaApi {

    @Operation(summary = "Consultar cuenta por identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
        @ApiResponse(responseCode = "404", description = "Cuenta inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<CuentaResponseDTO> obtenerPorId(Long id);

    @Operation(summary = "Liquidar / Cerrar una cuenta (Pasa a PAGADA y libera la mesa a DISPONIBLE)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta cerrada y liquidada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuenta inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "422", description = "La cuenta no se encuentra en estado ABIERTA",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<CuentaResponseDTO> cerrarCuenta(Long id);
}
