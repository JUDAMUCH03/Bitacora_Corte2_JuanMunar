package co.edu.eci.dosw.restaurant.service;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.mapper.PlatoEntityMapper;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.persistence.entity.PlatoEntity;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;
import co.edu.eci.dosw.restaurant.service.impl.PlatoServiceImpl;
import co.edu.eci.dosw.restaurant.validator.IPlatoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceImplTest {

    @Mock
    private PlatoRepository platoRepository;

    @Mock
    private PlatoEntityMapper entityMapper;

    @Mock
    private IPlatoValidator validator;

    @InjectMocks
    private PlatoServiceImpl service;

    private Plato platoEntrada;
    private PlatoEntity platoEntity;

    @BeforeEach
    void setUp() {
        platoEntrada = Plato.builder()
                .nombre("Smoked Old Fashioned")
                .precio(45000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .descripcion("Bourbon premium con ahumado en roble")
                .build();

        platoEntity = PlatoEntity.builder()
                .id(1L)
                .nombre("Smoked Old Fashioned")
                .precio(45000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .descripcion("Bourbon premium con ahumado en roble")
                .build();
    }

    @Test
    @DisplayName("crear - guarda el cóctel, persiste en BD y lo retorna con ID")
    void crear_platoValido_guardaYRetornaConId() {
        when(entityMapper.toEntity(platoEntrada)).thenReturn(platoEntity);
        when(platoRepository.save(platoEntity)).thenReturn(platoEntity);
        when(entityMapper.toDomain(platoEntity)).thenReturn(
                Plato.builder().id(1L).nombre("Smoked Old Fashioned").precio(45000.0).categoria("COCTEL_AUTOR").disponible(true).build()
        );

        Plato resultado = service.crear(platoEntrada);

        assertNotNull(resultado.getId());
        assertEquals("Smoked Old Fashioned", resultado.getNombre());
        assertTrue(resultado.estaDisponible());
        verify(validator).validarNombreUnico("Smoked Old Fashioned");
        verify(platoRepository).save(platoEntity);
    }

    @Test
    @DisplayName("crear - cuando el validador detecta duplicado, propaga ConflictoException")
    void crear_nombreDuplicado_lanzaConflictoException() {
        doThrow(new ConflictoException("Nombre duplicado"))
                .when(validator).validarNombreUnico("Smoked Old Fashioned");

        assertThrows(ConflictoException.class, () -> service.crear(platoEntrada));
        verify(platoRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorId - ID existente retorna el plato correspondiente")
    void obtenerPorId_existente_retornaPlato() {
        when(platoRepository.findById(1L)).thenReturn(Optional.of(platoEntity));
        when(entityMapper.toDomain(platoEntity)).thenReturn(platoEntrada);

        Plato hallado = service.obtenerPorId(1L);

        assertNotNull(hallado);
        assertEquals("Smoked Old Fashioned", hallado.getNombre());
        verify(platoRepository).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - ID inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_noExiste_lanzaRecursoNoEncontradoException() {
        when(platoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerTodos - sin registros devuelve lista vacía")
    void obtenerTodos_sinElementos_devuelveListaVacia() {
        when(platoRepository.findAll()).thenReturn(List.of());

        List<Plato> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(platoRepository).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - con registros devuelve la lista mapeada a dominio")
    void obtenerTodos_conElementos_devuelveLista() {
        when(platoRepository.findAll()).thenReturn(List.of(platoEntity));
        when(entityMapper.toDomain(platoEntity)).thenReturn(platoEntrada);

        List<Plato> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Smoked Old Fashioned", resultado.get(0).getNombre());
        verify(platoRepository).findAll();
    }

    @Test
    @DisplayName("obtenerDisponibles - consulta platos disponibles en el repositorio")
    void obtenerDisponibles_retornaSoloDisponibles() {
        when(platoRepository.findByDisponibleTrue()).thenReturn(List.of(platoEntity));
        when(entityMapper.toDomain(platoEntity)).thenReturn(platoEntrada);

        List<Plato> resultado = service.obtenerDisponibles();

        assertEquals(1, resultado.size());
        assertEquals("Smoked Old Fashioned", resultado.get(0).getNombre());
        verify(platoRepository).findByDisponibleTrue();
    }

    @Test
    @DisplayName("obtenerPorCategoria - filtra ignorando mayúsculas y minúsculas vía repositorio")
    void obtenerPorCategoria_retornaCategoriaSolicitada() {
        when(platoRepository.findByCategoriaIgnoreCase("MOCKTAIL")).thenReturn(List.of(platoEntity));
        when(entityMapper.toDomain(platoEntity)).thenReturn(platoEntrada);

        List<Plato> resultado = service.obtenerPorCategoria("MOCKTAIL");

        assertEquals(1, resultado.size());
        verify(platoRepository).findByCategoriaIgnoreCase("MOCKTAIL");
    }

    @Test
    @DisplayName("actualizar - actualiza los campos cuando los datos son válidos")
    void actualizar_platoExistente_actualizaCampos() {
        Plato actualizacion = Plato.builder()
                .nombre("Smoked Old Fashioned Reserve")
                .precio(52000.0)
                .categoria("COCTEL_AUTOR")
                .descripcion("Edición especial 12 años")
                .disponible(true)
                .build();

        PlatoEntity entityActualizada = PlatoEntity.builder()
                .id(1L)
                .nombre("Smoked Old Fashioned Reserve")
                .precio(52000.0)
                .categoria("COCTEL_AUTOR")
                .descripcion("Edición especial 12 años")
                .disponible(true)
                .build();

        when(platoRepository.findById(1L)).thenReturn(Optional.of(platoEntity));
        when(platoRepository.save(any(PlatoEntity.class))).thenReturn(entityActualizada);
        when(entityMapper.toDomain(entityActualizada)).thenReturn(actualizacion);

        Plato modificado = service.actualizar(1L, actualizacion);

        assertEquals("Smoked Old Fashioned Reserve", modificado.getNombre());
        assertEquals(52000.0, modificado.getPrecio());
        verify(validator).validarNombreUnico("Smoked Old Fashioned Reserve", 1L);
        verify(platoRepository).save(platoEntity);
    }

    @Test
    @DisplayName("actualizar - ID inexistente lanza RecursoNoEncontradoException")
    void actualizar_noExiste_lanzaRecursoNoEncontradoException() {
        when(platoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, platoEntrada));
        verify(platoRepository, never()).save(any());
    }

    @Test
    @DisplayName("cambiarDisponibilidad - activa e inhabilita un cóctel correctamente")
    void cambiarDisponibilidad_alternaEstados() {
        when(platoRepository.findById(1L)).thenReturn(Optional.of(platoEntity));
        when(platoRepository.save(platoEntity)).thenReturn(platoEntity);
        when(entityMapper.toDomain(platoEntity)).thenReturn(platoEntrada);

        Plato actualizado = service.cambiarDisponibilidad(1L, false);

        assertNotNull(actualizado);
        assertFalse(platoEntity.getDisponible());
        verify(platoRepository).save(platoEntity);
    }

    @Test
    @DisplayName("cambiarDisponibilidad - ID inexistente lanza RecursoNoEncontradoException")
    void cambiarDisponibilidad_noExiste_lanzaRecursoNoEncontradoException() {
        when(platoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.cambiarDisponibilidad(999L, true));
        verify(platoRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - remueve el plato existente de la carta")
    void eliminar_platoExistente_loElimina() {
        when(platoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.eliminar(1L));
        verify(platoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - ID inexistente propaga RecursoNoEncontradoException")
    void eliminar_noExiste_lanzaRecursoNoEncontradoException() {
        when(platoRepository.existsById(888L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(888L));
        verify(platoRepository, never()).deleteById(any());
    }
}