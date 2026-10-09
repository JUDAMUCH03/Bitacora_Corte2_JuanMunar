package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;

import co.edu.eci.dosw.restaurant.dto.response.MesaResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.Mesa;
import co.edu.eci.dosw.restaurant.persistence.entity.MesaEntity;

@Mapper(componentModel = "spring")
public interface MesaMapper {

    Mesa toDomain(MesaEntity entity);

    MesaEntity toEntity(Mesa domain);

    MesaResponseDTO toResponse(Mesa domain);
}
