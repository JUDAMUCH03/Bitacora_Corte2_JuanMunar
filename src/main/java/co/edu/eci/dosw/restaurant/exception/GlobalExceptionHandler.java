package co.edu.eci.dosw.restaurant.exception;

import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.time.ZoneId;

/**
 * Interceptor centralizado de excepciones (Controller Advice).
 * Estandariza las respuestas de error y previene fugas de trazas internas.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * HTTP 400 - Entrada inválida (Falla en Bean Validation en RequestDTO).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String detalleErrores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Fallo de validación de entrada en [{}]: {}", request.getRequestURI(), detalleErrores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST.value(), "Bad Request", detalleErrores, request.getRequestURI()));
    }

    /**
     * HTTP 404 - Recurso inexistente en el sistema.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            RecursoNoEncontradoException ex,
            HttpServletRequest request) {

        log.warn("Recurso no encontrado en [{}]: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getRequestURI()));
    }

    /**
     * HTTP 409 - Conflicto de estado o duplicidad de negocio.
     */
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflicto(
            ConflictoException ex,
            HttpServletRequest request) {

        log.warn("Conflicto de negocio detectado en [{}]: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), request.getRequestURI()));
    }

    /**
     * HTTP 422 - Violación de reglas de dominio o transiciones de estado inválidas.
     */
    @ExceptionHandler({EstadoInvalidoException.class, ReglaDeNegocioException.class})
    public ResponseEntity<ErrorResponseDTO> handleNegocioInvalido(
            RuntimeException ex,
            HttpServletRequest request) {

        log.warn("Operación de negocio rechazada en [{}]: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Unprocessable Entity", ex.getMessage(), request.getRequestURI()));
    }

    /**
     * HTTP 500 - Error inesperado del servidor (Filtro de seguridad DevSecOps).
     * Devuelve un mensaje genérico para mitigar exposición de trazas internas (CWE-209).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneral(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error crítico no controlado en [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                        "Ha ocurrido un error interno en el servidor. Contacte al soporte técnico.", request.getRequestURI()));
    }

    private ErrorResponseDTO buildError(int status, String error, String message, String path) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC"))) // Zona horaria explícita
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}