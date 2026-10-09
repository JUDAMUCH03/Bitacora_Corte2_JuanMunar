package co.edu.eci.dosw.restaurant.validator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.repository.PlatoRepository;

@Component
@RequiredArgsConstructor
public class PlatoValidator implements IPlatoValidator {

    private final PlatoRepository platoRepository;

    @Override
    public void validarNombreUnico(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return; // La presencia de datos la garantiza el @NotBlank del DTO
        }

        if (platoRepository.existsByNombreIgnoreCase(nombre.trim())) {
            throw new ConflictoException(
                    String.format("Ya existe un cóctel o plato registrado con el nombre '%s' en la carta", nombre.trim())
            );
        }
    }

    @Override
    public void validarNombreUnico(String nombre, Long id) {
        if (nombre == null || nombre.isBlank()) {
            return; // La presencia de datos la garantiza el @NotBlank del DTO
        }

        if (platoRepository.existsByNombreIgnoreCaseAndIdNot(nombre.trim(), id)) {
            throw new ConflictoException(
                    String.format("Ya existe un cóctel o plato registrado con el nombre '%s' en la carta", nombre.trim())
            );
        }
    }

    @Override
    public void validarDisponibleParaPedido(Plato plato) {
        if (!plato.estaDisponible()) {
            throw new ReglaDeNegocioException(
                    String.format("El cóctel '%s' se encuentra agotado en barra", plato.getNombre())
            );
        }
    }
}