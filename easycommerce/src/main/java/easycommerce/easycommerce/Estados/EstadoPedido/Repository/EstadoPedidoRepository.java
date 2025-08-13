package easycommerce.easycommerce.Estados.EstadoPedido.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Estados.EstadoPedido.Model.EstadoPedido;


@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido,Long> {

}
