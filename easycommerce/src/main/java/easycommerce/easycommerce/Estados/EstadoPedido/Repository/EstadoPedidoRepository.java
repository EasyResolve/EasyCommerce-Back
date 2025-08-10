package backend_gnr.backend_gnr.Estados.EstadoPedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend_gnr.backend_gnr.Estados.EstadoPedido.model.EstadoPedido;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido,Long> {

}
