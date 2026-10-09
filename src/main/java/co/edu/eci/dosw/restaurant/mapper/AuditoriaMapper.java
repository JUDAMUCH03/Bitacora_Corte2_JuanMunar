package co.edu.eci.dosw.restaurant.mapper;

import org.mapstruct.Mapper;

import co.edu.eci.dosw.restaurant.dto.response.EventoAuditoriaResponseDTO;
import co.edu.eci.dosw.restaurant.persistence.document.EventoAuditoria;

@Mapper(componentModel = "spring")
public interface AuditoriaMapper {

    EventoAuditoriaResponseDTO toResponse(EventoAuditoria document);
}
