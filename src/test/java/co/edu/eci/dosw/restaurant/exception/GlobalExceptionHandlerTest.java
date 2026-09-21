package co.edu.eci.dosw.restaurant.exception;

import co.edu.eci.dosw.restaurant.dto.response.ErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/platos/1");
    }

    @Test
    @DisplayName("handleNotFound - retorna 404 y estructura estandarizada")
    void handleNotFound_retorna404() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Plato", 1L);
        ResponseEntity<ErrorResponseDTO> response = handler.handleNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("/api/v1/platos/1", response.getBody().getPath());
    }

    @Test
    @DisplayName("handleConflicto - retorna 409 y mensaje de negocio")
    void handleConflicto_retorna409() {
        ConflictoException ex = new ConflictoException("Nombre duplicado");
        ResponseEntity<ErrorResponseDTO> response = handler.handleConflicto(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Nombre duplicado", response.getBody().getMessage());
    }

    @Test
    @DisplayName("handleNegocioInvalido - retorna 422 para reglas de dominio")
    void handleNegocioInvalido_retorna422() {
        ReglaDeNegocioException ex = new ReglaDeNegocioException("Mocktail no admite alcohol");
        ResponseEntity<ErrorResponseDTO> response = handler.handleNegocioInvalido(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(422, response.getBody().getStatus());
    }

    @Test
    @DisplayName("handleGeneral - retorna 500 sin exponer detalles sensibles (DevSecOps)")
    void handleGeneral_retorna500Generico() {
        Exception ex = new RuntimeException("Error inesperado de base de datos");
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneral(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Ha ocurrido un error interno en el servidor. Contacte al soporte técnico.",
                response.getBody().getMessage());
    }
}