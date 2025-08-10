package easycommerce.easycommerce.Estados.Estado.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Estados.Estado.Model.Estado;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;

public interface EstadoService {
    List<Estado> findAll();
    Optional<Estado> findById(Long id);
    Estado save(Estado estado) throws EntityAlreadyExistsException;
    void delete(Long id);
}
