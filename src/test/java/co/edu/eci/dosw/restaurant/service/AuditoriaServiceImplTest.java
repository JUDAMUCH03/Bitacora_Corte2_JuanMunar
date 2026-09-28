package co.edu.eci.dosw.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.eci.dosw.restaurant.mapper.EventoMapper;
import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument;
import co.edu.eci.dosw.restaurant.repository.EventoRestauranteRepository;
import co.edu.eci.dosw.restaurant.service.impl.AuditoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceImplTest {

    @Mock
    private EventoRestauranteRepository eventoRepository;

    @Mock
    private EventoMapper eventoMapper;

    @InjectMocks
    private AuditoriaServiceImpl auditoriaService;

    private EventoRestaurante evento;
    private EventoRestauranteDocument documento;

    @BeforeEach
    void setUp() {
        evento = EventoRestaurante.builder()
                .tipo("PLATO_CREADO")
                .entidadTipo("PLATO")
                .entidadId(10L)
                .descripcion("Creación de Gin Tonic")
                .usuario("SISTEMA")
                .metadatos(Map.of("nombre", "Gin Tonic"))
                .build();

        documento = EventoRestauranteDocument.builder()
                .id("mongo-id-123")
                .tipo("PLATO_CREADO")
                .entidadTipo("PLATO")
                .entidadId(10L)
                .descripcion("Creación de Gin Tonic")
                .usuario("SISTEMA")
                .timestamp(LocalDateTime.now())
                .metadatos(Map.of("nombre", "Gin Tonic"))
                .build();
    }

    @Test
    @DisplayName("registrarEvento - persiste el documento en Mongo y lo retorna mapeado")
    void registrarEvento_conTimestampNulo_asignaTimestampYGuarda() {
        when(eventoMapper.toDocument(evento)).thenReturn(documento);
        when(eventoRepository.save(documento)).thenReturn(documento);
        when(eventoMapper.toDomain(documento)).thenReturn(
                EventoRestaurante.builder().id("mongo-id-123").tipo("PLATO_CREADO").build()
        );

        EventoRestaurante guardado = auditoriaService.registrarEvento(evento);

        assertNotNull(guardado);
        assertEquals("mongo-id-123", guardado.getId());
        assertNotNull(evento.getTimestamp());
        verify(eventoRepository).save(documento);
    }

    @Test
    @DisplayName("obtenerEventosPorEntidad - consulta por entidadTipo y entidadId")
    void obtenerEventosPorEntidad_retornaListaMapeada() {
        when(eventoRepository.findByEntidadTipoAndEntidadId("PLATO", 10L)).thenReturn(List.of(documento));
        when(eventoMapper.toDomain(documento)).thenReturn(evento);

        List<EventoRestaurante> resultado = auditoriaService.obtenerEventosPorEntidad("PLATO", 10L);

        assertEquals(1, resultado.size());
        assertEquals("PLATO_CREADO", resultado.get(0).getTipo());
        verify(eventoRepository).findByEntidadTipoAndEntidadId("PLATO", 10L);
    }

    @Test
    @DisplayName("obtenerEventosPorTipoYRango - consulta por tipo y rango de fechas")
    void obtenerEventosPorTipoYRango_retornaListaMapeada() {
        LocalDateTime desde = LocalDateTime.now().minusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(1);

        when(eventoRepository.findByTipoAndTimestampBetween("PLATO_CREADO", desde, hasta)).thenReturn(List.of(documento));
        when(eventoMapper.toDomain(documento)).thenReturn(evento);

        List<EventoRestaurante> resultado = auditoriaService.obtenerEventosPorTipoYRango("PLATO_CREADO", desde, hasta);

        assertEquals(1, resultado.size());
        verify(eventoRepository).findByTipoAndTimestampBetween("PLATO_CREADO", desde, hasta);
    }
}
