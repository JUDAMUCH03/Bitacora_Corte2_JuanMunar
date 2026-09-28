package co.edu.eci.dosw.restaurant.repository;

import co.edu.eci.dosw.restaurant.persistence.document.EventoRestauranteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRestauranteRepository extends MongoRepository<EventoRestauranteDocument, String> {

    List<EventoRestauranteDocument> findByEntidadTipoAndEntidadId(String entidadTipo, Long entidadId);

    List<EventoRestauranteDocument> findByTipoAndTimestampBetween(String tipo, LocalDateTime desde, LocalDateTime hasta);
}
