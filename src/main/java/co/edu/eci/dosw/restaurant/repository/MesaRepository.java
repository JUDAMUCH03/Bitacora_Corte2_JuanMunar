package co.edu.eci.dosw.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.eci.dosw.restaurant.persistence.entity.MesaEntity;

@Repository
public interface MesaRepository extends JpaRepository<MesaEntity, Long> {

    Optional<MesaEntity> findByNumero(Integer numero);
}
