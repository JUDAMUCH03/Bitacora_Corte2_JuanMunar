package co.edu.eci.dosw.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.eci.dosw.restaurant.persistence.entity.ReservaEntity;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    /** Reservas ordenadas de la más reciente/lejana a la más antigua (fecha y hora descendente). */
    List<ReservaEntity> findAllByOrderByFechaDescHoraDesc();
}
