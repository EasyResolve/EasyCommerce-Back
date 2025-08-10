package backend_gnr.backend_gnr.Estados.EstadoPedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import backend_gnr.backend_gnr.Estados.EstadoPedido.model.EstadoPedido;
import backend_gnr.backend_gnr.Estados.EstadoPedido.repository.EstadoPedidoRepository;

@Service
public class EstadoPedidoServiceIMPL implements EstadoPedidoService {

    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoServiceIMPL(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    @Override
    public List<EstadoPedido> findAll() {
        return estadoPedidoRepository.findAll();
    }

    @Override
    public Optional<EstadoPedido> findById(Long id) {
        return estadoPedidoRepository.findById(id);
    }

    @Override
    public EstadoPedido save(EstadoPedido estadoPedido) {
        return estadoPedidoRepository.save(estadoPedido);
    }

    @Override
    public void delete(Long id) {
        estadoPedidoRepository.deleteById(id);
    }

    
}
