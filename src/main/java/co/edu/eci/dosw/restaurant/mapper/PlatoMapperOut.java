package co.edu.eci.dosw.restaurant.mapper;

import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlatoMapperOut {

    PlatoResponseDTO toResponse(Plato plato);

    List<PlatoResponseDTO> toResponseList(List<Plato> platos);
}