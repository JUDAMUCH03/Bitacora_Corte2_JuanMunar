package co.edu.eci.dosw.restaurant.mapper;

import co.edu.eci.dosw.restaurant.dto.request.CrearPlatoRequest;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponse;
import co.edu.eci.dosw.restaurant.model.domain.Plato;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-15T23:48:54-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class PlatoMapperImpl implements PlatoMapper {

    @Override
    public Plato toDomain(CrearPlatoRequest request) {
        if ( request == null ) {
            return null;
        }

        Plato plato = new Plato();

        return plato;
    }

    @Override
    public PlatoResponse toResponse(Plato plato) {
        if ( plato == null ) {
            return null;
        }

        String nombre = null;
        double precio = 0.0d;

        PlatoResponse platoResponse = new PlatoResponse( nombre, precio );

        return platoResponse;
    }
}
