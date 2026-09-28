package co.edu.eci.dosw.restaurant.mapper;

import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.persistence.entity.PlatoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatoEntityMapper {

    Plato toDomain(PlatoEntity entity);

    @Mapping(target = "creadoEn", ignore = true)
    PlatoEntity toEntity(Plato domain);
}
