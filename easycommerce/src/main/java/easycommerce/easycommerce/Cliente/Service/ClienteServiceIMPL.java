package easycommerce.easycommerce.Cliente.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cliente.Repository.ClienteRepository;


@Service
public class ClienteServiceIMPL implements ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteServiceIMPL(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente save(Cliente cliente){
        
        Optional<Cliente> clienteBd = clienteRepository.findByDocumento(cliente.getDocumento());
        
        return clienteBd.orElseGet(() -> clienteRepository.save(cliente));
    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
