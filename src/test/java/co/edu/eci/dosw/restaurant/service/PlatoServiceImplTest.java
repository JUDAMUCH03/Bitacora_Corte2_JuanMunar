package co.edu.eci.dosw.restaurant.service;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.RecursoNoEncontradoException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceImplTest {

    @Mock
    private IPlatoValidator validator;

    @InjectMocks
    private PlatoServiceImpl service;

    private Plato platoEntrada;

    @BeforeEach
    void setUp() {
        platoEntrada = Plato.builder()
                .nombre("Smoked Old Fashioned")
                .precio(45000.0)
                .categoria("COCTEL_AUTOR")
                .disponible(true)
                .descripcion("Bourbon premium con ahumado en roble")
                .build();
    }

    @Test
    @DisplayName("crear - guarda el cóctel, asigna ID autoincremental y lo retorna")
    void crear_platoValido_guardaYRetornaConId() {
        Plato resultado = service.crear(platoEntrada);

        assertNotNull(resultado.getId());
        assertEquals("Smoked Old Fashioned", resultado.getNombre());
        assertTrue(resultado.estaDisponible());
        verify(validator, times(1)).validarNombreUnico(eq("Smoked Old Fashioned"), any());
    }

    @Test
    @DisplayName("crear - cuando el validador detecta duplicado, propaga ConflictoException")
    void crear_nombreDuplicado_lanzaConflictoException() {
        doThrow(new ConflictoException("Nombre duplicado"))
                .when(validator).validarNombreUnico(any(), any());

        assertThrows(ConflictoException.class, () -> service.crear(platoEntrada));
    }

    @Test
    @DisplayName("obtenerPorId - ID existente retorna el plato correspondiente")
    void obtenerPorId_existente_retornaPlato() {
        Plato creado = service.crear(platoEntrada);
        Plato hallado = service.obtenerPorId(creado.getId());

        assertNotNull(hallado);
        assertEquals(creado.getId(), hallado.getId());
        assertEquals("Smoked Old Fashioned", hallado.getNombre());
    }

    @Test
    @DisplayName("obtenerPorId - ID inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_noExiste_lanzaRecursoNoEncontradoException() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerTodos - sin registros devuelve lista vacía")
    void obtenerTodos_sinElementos_devuelveListaVacia() {
        List<Plato> resultado = service.obtenerTodos();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerDisponibles - filtra correctamente y excluye cócteles agotados")
    void obtenerDisponibles_filtraSoloDisponibles() {
        Plato disponible = Plato.builder().nombre("Negroni").precio(38000.0).disponible(true).build();
        Plato agotado = Plato.builder().nombre("Dry Martini").precio(40000.0).disponible(false).build();

        service.crear(disponible);
        service.crear(agotado);

        List<Plato> resultado = service.obtenerDisponibles();

        assertEquals(1, resultado.size());
        assertEquals("Negroni", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("obtenerPorCategoria - filtra ignorando mayúsculas y minúsculas")
    void obtenerPorCategoria_retornaSoloCategoriaSolicitada() {
        Plato coctel = Plato.builder().nombre("Mojito").categoria("COCTEL_AUTOR").disponible(true).build();
        Plato mocktail = Plato.builder().nombre("Virgin Mule").categoria("MOCKTAIL").disponible(true).build();

        service.crear(coctel);
        service.crear(mocktail);

        List<Plato> resultado = service.obtenerPorCategoria("mocktail");

        assertEquals(1, resultado.size());
        assertEquals("Virgin Mule", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("actualizar - actualiza los campos cuando los datos son válidos")
    void actualizar_platoExistente_actualizaCampos() {
        Plato creado = service.crear(platoEntrada);
        Plato actualizacion = Plato.builder()
                .nombre("Smoked Old Fashioned Reserve")
                .precio(52000.0)
                .categoria("COCTEL_AUTOR")
                .descripcion("Edición especial 12 años")
                .build();

        Plato modificado = service.actualizar(creado.getId(), actualizacion);

        assertEquals("Smoked Old Fashioned Reserve", modificado.getNombre());
        assertEquals(52000.0, modificado.getPrecio());
        assertEquals("Edición especial 12 años", modificado.getDescripcion());
        verify(validator, atLeastOnce()).validarNombreUnico(eq("Smoked Old Fashioned Reserve"), any());
    }

    @Test
    @DisplayName("cambiarDisponibilidad - activa e inhabilita un cóctel correctamente")
    void cambiarDisponibilidad_alternaEstados() {
        Plato creado = service.crear(platoEntrada);

        // Desactivar
        Plato inactivo = service.cambiarDisponibilidad(creado.getId(), false);
        assertFalse(inactivo.estaDisponible());

        // Reactivar
        Plato activo = service.cambiarDisponibilidad(creado.getId(), true);
        assertTrue(activo.estaDisponible());
    }

    @Test
    @DisplayName("eliminar - remueve el plato existente de la carta")
    void eliminar_platoExistente_loElimina() {
        Plato creado = service.crear(platoEntrada);
        Long id = creado.getId();

        assertDoesNotThrow(() -> service.eliminar(id));
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(id));
    }

    @Test
    @DisplayName("eliminar - ID inexistente propaga RecursoNoEncontradoException")
    void eliminar_noExiste_lanzaRecursoNoEncontradoException() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(888L));
    }
}