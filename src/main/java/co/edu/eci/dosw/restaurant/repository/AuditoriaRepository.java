package co.edu.eci.dosw.restaurant.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.eci.dosw.restaurant.persistence.document.EventoAuditoria;

@Repository
public interface AuditoriaRepository extends MongoRepository<EventoAuditoria, String> {

    List<EventoAuditoria> findTop50ByOrderByTimestampDesc();

    List<EventoAuditoria> findByEntidadId(String entidadId);

    List<EventoAuditoria> findByTipoEvento(String tipoEvento);
}
