package easycommerce.easycommerce.Estados.EstadoPedido.Service;

import java.util.List;
import java.util.Optional;

import backend_gnr.backend_gnr.Estados.EstadoPedido.model.EstadoPedido;

public interface EstadoPedidoService {
    List<EstadoPedido> findAll();
    Optional<EstadoPedido> findById(Long id);
    EstadoPedido save(EstadoPedido estadoPedido);
    void delete(Long id);
}
