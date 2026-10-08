package co.edu.eci.dosw.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoPedido;
import co.edu.eci.dosw.restaurant.persistence.entity.PedidoEntity;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByEstado(EstadoPedido estado);

    List<PedidoEntity> findByIdMesa(Long idMesa);
}
