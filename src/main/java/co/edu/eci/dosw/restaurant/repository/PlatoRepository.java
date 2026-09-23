package co.edu.eci.dosw.restaurant.repository;

import co.edu.eci.dosw.restaurant.model.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<PlatoEntity, Long> {
    
    List<PlatoEntity> findByDisponibleTrue();
    List<PlatoEntity> findByCategoriaIgnoreCase(String categoria);
    boolean           existsByNombreIgnoreCase(String nombre);
    boolean           existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
