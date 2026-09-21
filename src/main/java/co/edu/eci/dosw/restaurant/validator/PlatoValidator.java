package co.edu.eci.dosw.restaurant.validator;

import co.edu.eci.dosw.restaurant.exception.ConflictoException;
import co.edu.eci.dosw.restaurant.exception.ReglaDeNegocioException;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PlatoValidator implements IPlatoValidator {

    @Override
    public void validarNombreUnico(String nombre, Collection<Plato> platosExistentes) {
        if (nombre == null || nombre.isBlank()) {
            return; // La presencia de datos la garantiza el @NotBlank del DTO
        }

        boolean duplicado = platosExistentes.stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre.trim()));

        if (duplicado) {
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