package co.edu.eci.dosw.restaurant.repository;

import co.edu.eci.dosw.restaurant.model.domain.Plato;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatoRepository {
    List<Plato> findAll();
}
