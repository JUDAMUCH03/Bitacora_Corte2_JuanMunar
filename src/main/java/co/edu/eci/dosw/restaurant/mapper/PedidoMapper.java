package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.eci.dosw.restaurant.dto.request.PedidoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.PedidoResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.Pedido;
import co.edu.eci.dosw.restaurant.persistence.entity.PedidoEntity;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    Pedido toDomain(PedidoRequestDTO dto);

    Pedido toDomain(PedidoEntity entity);

    PedidoEntity toEntity(Pedido domain);

    PedidoResponseDTO toResponse(Pedido domain);
}
