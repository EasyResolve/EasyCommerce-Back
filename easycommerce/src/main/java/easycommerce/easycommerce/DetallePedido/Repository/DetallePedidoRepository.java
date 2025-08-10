package easycommerce.easycommerce.DetallePedido.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.DetallePedido.Model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>{

}
