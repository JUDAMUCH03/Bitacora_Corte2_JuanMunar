package co.edu.eci.dosw.restaurant.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.Plato;

@Mapper(componentModel = "spring")
public interface PlatoMapperOut {

    PlatoResponseDTO toResponse(Plato plato);

    List<PlatoResponseDTO> toResponseList(List<Plato> platos);
}