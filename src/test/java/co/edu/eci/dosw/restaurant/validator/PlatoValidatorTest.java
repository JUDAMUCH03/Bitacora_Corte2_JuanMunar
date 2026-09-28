package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoValidatorTest {

    @Mock
    private PlatoRepository platoRepository;

    @InjectMocks
    private PlatoValidator validator;

    @Test
    @DisplayName("validarNombreUnico - nombre nuevo no registrado no lanza excepción")
    void validarNombreUnico_nombreNuevo_noLanzaExcepcion() {
        when(platoRepository.existsByNombreIgnoreCase("Bramble")).thenReturn(false);

        assertDoesNotThrow(() -> validator.validarNombreUnico("Bramble"));
        verify(platoRepository).existsByNombreIgnoreCase("Bramble");
    }

    @Test
    @DisplayName("validarNombreUnico - nombre nulo o vacío no interactúa con repositorio ni lanza excepción")
    void validarNombreUnico_nombreVacioONulo_noHaceNada() {
        assertDoesNotThrow(() -> validator.validarNombreUnico(null));
        assertDoesNotThrow(() -> validator.validarNombreUnico("   "));
        verifyNoInteractions(platoRepository);
    }

    @Test
    @DisplayName("validarNombreUnico - nombre duplicado (case-insensitive con espacios) lanza ConflictoException")
    void validarNombreUnico_nombreDuplicado_lanzaConflictoException() {
        when(platoRepository.existsByNombreIgnoreCase("penicillin")).thenReturn(true);

        assertThrows(ConflictoException.class, () ->
                validator.validarNombreUnico("  penicillin  ")
        );
        verify(platoRepository).existsByNombreIgnoreCase("penicillin");
    }

    @Test
    @DisplayName("validarNombreUnico con ID - nombre único para otros registros no lanza excepción")
    void validarNombreUnico_conId_noLanzaExcepcion() {
        when(platoRepository.existsByNombreIgnoreCaseAndIdNot("Mojito", 1L)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validarNombreUnico("Mojito", 1L));
        verify(platoRepository).existsByNombreIgnoreCaseAndIdNot("Mojito", 1L);
    }

    @Test
    @DisplayName("validarNombreUnico con ID - nombre duplicado en otro ID lanza ConflictoException")
    void validarNombreUnico_conId_duplicado_lanzaConflictoException() {
        when(platoRepository.existsByNombreIgnoreCaseAndIdNot("Mojito", 1L)).thenReturn(true);

        assertThrows(ConflictoException.class, () ->
                validator.validarNombreUnico("Mojito", 1L)
        );
        verify(platoRepository).existsByNombreIgnoreCaseAndIdNot("Mojito", 1L);
    }

    @Test
    @DisplayName("validarNombreUnico con ID - nombre nulo o vacío no interactúa con repositorio")
    void validarNombreUnico_conId_vacioONulo_noHaceNada() {
        assertDoesNotThrow(() -> validator.validarNombreUnico(null, 1L));
        assertDoesNotThrow(() -> validator.validarNombreUnico("   ", 1L));
        verifyNoInteractions(platoRepository);
    }

    @Test
    @DisplayName("validarDisponibleParaPedido - trago agotado en barra lanza ReglaDeNegocioException")
    void validarDisponibleParaPedido_platoInactivo_lanzaReglaDeNegocioException() {
        Plato agotado = Plato.builder().nombre("Gin Tonic").disponible(false).build();

        assertThrows(ReglaDeNegocioException.class, () ->
                validator.validarDisponibleParaPedido(agotado)
        );
    }

    @Test
    @DisplayName("validarDisponibleParaPedido - trago disponible no lanza excepción")
    void validarDisponibleParaPedido_platoActivo_noLanzaExcepcion() {
        Plato activo = Plato.builder().nombre("Gin Tonic").disponible(true).build();

        assertDoesNotThrow(() -> validator.validarDisponibleParaPedido(activo));
    }
}