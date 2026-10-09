package co.edu.eci.dosw.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoCuenta;
import co.edu.eci.dosw.restaurant.persistence.entity.CuentaEntity;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaEntity, Long> {

    List<CuentaEntity> findByIdMesa(Long idMesa);

    Optional<CuentaEntity> findByIdMesaAndEstado(Long idMesa, EstadoCuenta estado);

    boolean existsByIdMesaAndEstado(Long idMesa, EstadoCuenta estado);
}
