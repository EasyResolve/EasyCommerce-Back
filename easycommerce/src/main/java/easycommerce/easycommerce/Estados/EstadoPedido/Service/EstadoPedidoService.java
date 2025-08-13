package easycommerce.easycommerce.Estados.EstadoPedido.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Estados.EstadoPedido.Model.EstadoPedido;


public interface EstadoPedidoService {
    List<EstadoPedido> findAll();
    Optional<EstadoPedido> findById(Long id);
    EstadoPedido save(EstadoPedido estadoPedido);
    void delete(Long id);
}
