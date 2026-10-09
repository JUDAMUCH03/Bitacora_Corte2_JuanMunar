package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.eci.dosw.restaurant.dto.request.ItemPedidoRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.ItemPedidoResponseDTO;
import co.edu.eci.dosw.restaurant.model.domain.ItemPedido;
import co.edu.eci.dosw.restaurant.persistence.entity.ItemPedidoEntity;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombrePlato", ignore = true)
    @Mapping(target = "precioCongelado", ignore = true)
    @Mapping(target = "spiritModificationsCount", constant = "0")
    ItemPedido toDomain(ItemPedidoRequestDTO dto);

    ItemPedido toDomain(ItemPedidoEntity entity);

    ItemPedidoEntity toEntity(ItemPedido domain);

    ItemPedidoResponseDTO toResponse(ItemPedido domain);
}
