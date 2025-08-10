package easycommerce.easycommerce.Cliente.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;


public interface ClienteService {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente) throws EntityAlreadyExistsException;
    void delete(Long id);
    Cliente updateCliente(Cliente cliente);
}
