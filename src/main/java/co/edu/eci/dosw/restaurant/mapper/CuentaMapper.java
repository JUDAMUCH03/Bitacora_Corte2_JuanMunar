package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;

import co.edu.eci.dosw.restaurant.dto.response.CuentaResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.Cuenta;
import co.edu.eci.dosw.restaurant.persistence.entity.CuentaEntity;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    Cuenta toDomain(CuentaEntity entity);

    CuentaEntity toEntity(Cuenta domain);

    CuentaResponseDTO toResponse(Cuenta domain);
}
