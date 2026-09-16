package co.edu.eci.dosw.restaurant.mapper;

import co.edu.eci.dosw.restaurant.dto.request.CrearPlatoRequest;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponse;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatoMapper {
    Plato toDomain(CrearPlatoRequest request);

    PlatoResponse toResponse(Plato plato);
}
