package easycommerce.easycommerce.Caja.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Caja.Model.Caja;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;


public interface CajaService {
    List<Caja> findAll();
    Optional<Caja> findById(Long id);
    Caja save(Caja caja) throws EntityAlreadyExistsException;
    void delete(Long id);
}
