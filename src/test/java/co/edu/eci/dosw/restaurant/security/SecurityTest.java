package co.edu.eci.dosw.restaurant.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.edu.eci.dosw.restaurant.config.DataInitializer;
import co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import co.edu.eci.dosw.restaurant.mapper.PlatoMapperIn;
import co.edu.eci.dosw.restaurant.mapper.PlatoMapperOut;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.repository.EventoRestauranteRepository;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;
import co.edu.eci.dosw.restaurant.repository.UsuarioRepository;
import co.edu.eci.dosw.restaurant.service.IAuditoriaService;
import co.edu.eci.dosw.restaurant.service.IPlatoService;

/**
 * Pruebas automatizadas de seguridad e integración para Blue Velvet (Guía S10 DOSW).
 * Valida protección de endpoints, RBAC y denegación de accesos no autorizados.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPlatoService platoService;

    @MockBean
    private PlatoMapperIn platoMapperIn;

    @MockBean
    private PlatoMapperOut platoMapperOut;

    @MockBean
    private PlatoRepository platoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EventoRestauranteRepository eventoRestauranteRepository;

    @MockBean
    private IAuditoriaService auditoriaService;

    @MockBean
    private DataInitializer dataInitializer;

    private static final String PAYLOAD_VALIDO = """
            {
              "nombre": "Smoked Old Fashioned",
              "descripcion": "Bourbon ahumado con corteza de roble y naranja",
              "precio": 38000.0,
              "categoria": "COCTEL_AUTOR",
              "tiempoPreparacionMinutos": 7,
              "disponible": true
            }
            """;

    @Test
    @DisplayName("Caso 1: Invocar POST /api/v1/platos sin Authorization retorna 401 Unauthorized")
    void crearPlato_sinAutenticacion_retorna401() throws Exception {
        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_VALIDO))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Caso 2: Invocar POST /api/v1/platos con rol CLIENTE retorna 403 Forbidden")
    @WithMockUser(username = "cliente@bluevelvet.com", roles = {"CLIENTE"})
    void crearPlato_conRolCliente_retorna403() throws Exception {
        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_VALIDO))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Caso 3a: Invocar POST /api/v1/platos con rol CHEF no debe retornar 401 ni 403 (retorna 201 Created)")
    @WithMockUser(username = "chef@bluevelvet.com", roles = {"CHEF"})
    void crearPlato_conRolChef_autorizado() throws Exception {
        Plato mockPlato = Plato.builder()
                .id(1L)
                .nombre("Smoked Old Fashioned")
                .precio(38000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .build();

        PlatoResponseDTO mockResponse = PlatoResponseDTO.builder()
                .id(1L)
                .nombre("Smoked Old Fashioned")
                .precio(38000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .build();

        when(platoMapperIn.toDomain(any(PlatoRequestDTO.class))).thenReturn(mockPlato);
        when(platoService.crear(any(Plato.class))).thenReturn(mockPlato);
        when(platoMapperOut.toResponse(any(Plato.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_VALIDO))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Caso 3b: Invocar POST /api/v1/platos con rol ADMIN no debe retornar 401 ni 403 (retorna 201 Created)")
    @WithMockUser(username = "admin@bluevelvet.com", roles = {"ADMIN"})
    void crearPlato_conRolAdmin_autorizado() throws Exception {
        Plato mockPlato = Plato.builder()
                .id(2L)
                .nombre("Smoked Old Fashioned")
                .precio(38000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .build();

        PlatoResponseDTO mockResponse = PlatoResponseDTO.builder()
                .id(2L)
                .nombre("Smoked Old Fashioned")
                .precio(38000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .build();

        when(platoMapperIn.toDomain(any(PlatoRequestDTO.class))).thenReturn(mockPlato);
        when(platoService.crear(any(Plato.class))).thenReturn(mockPlato);
        when(platoMapperOut.toResponse(any(Plato.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_VALIDO))
                .andExpect(status().isCreated());
    }
}
