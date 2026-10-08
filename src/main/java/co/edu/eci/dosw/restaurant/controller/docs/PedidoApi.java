package co.edu.eci.dosw.restaurant.controller.docs;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import co.edu.eci.dosw.restaurant.dto.request.ActualizarEstadoPedidoDTO;
import co.edu.eci.dosw.restaurant.dto.request.ModificarDestiladoDTO;
import co.edu.eci.dosw.restaurant.dto.request.PedidoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;
import co.edu.eci.dosw.restaurant.dto.response.PedidoResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;

@Tag(name = "Comandas y KDS", description = "Gestión de pedidos de sala/barra y flujo de estados en cocina/coctelería (KDS)")
public interface PedidoApi {

    @Operation(summary = "Crear comanda / pedido de mesa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comanda creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o excede límite de garnishes",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Plato no encontrado en catálogo",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PedidoResponseDTO> crear(PedidoRequestDTO dto);

    @Operation(summary = "Consultar comanda por identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comanda encontrada"),
        @ApiResponse(responseCode = "404", description = "Comanda inexistente",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PedidoResponseDTO> obtenerPorId(Long id);

    @Operation(summary = "Listar comandas con filtros opcionales por estado o mesa")
    @ApiResponse(responseCode = "200", description = "Listado de comandas")
    ResponseEntity<List<PedidoResponseDTO>> listar(EstadoPedido estado, Long idMesa);

    @Operation(summary = "Actualizar estado en el flujo KDS (RECIBIDO -> EN_PREPARACION -> LISTO -> ENTREGADO / CANCELADO)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de comanda actualizado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Comanda inexistente"),
        @ApiResponse(responseCode = "422", description = "Transición de estado no permitida por regla de negocio",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PedidoResponseDTO> actualizarEstado(Long id, ActualizarEstadoPedidoDTO dto);

    @Operation(summary = "Modificar destilado base de un cóctel (mutación permitida una sola vez en estado RECIBIDO)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Destilado modificado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Comanda o ítem inexistente"),
        @ApiResponse(responseCode = "422", description = "Mutación no permitida (estado avanzado o límite alcanzado)",
                     content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PedidoResponseDTO> modificarDestilado(Long id, Long itemId, ModificarDestiladoDTO dto);
}
