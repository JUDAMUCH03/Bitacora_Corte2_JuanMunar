package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.eci.dosw.restaurant.model.domain.EventoRestaurante;
import co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "id", ignore = true)
    EventoRestauranteDocument toDocument(EventoRestaurante domain);

    EventoRestaurante toDomain(EventoRestauranteDocument doc);
}
