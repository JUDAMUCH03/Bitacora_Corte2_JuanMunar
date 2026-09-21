package co.edu.eci.dosw.restaurant.mapper.in;

import co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatoMapperIn {
    Plato toDomain(PlatoRequestDTO request);
}
