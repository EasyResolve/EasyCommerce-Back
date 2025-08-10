package easycommerce.easycommerce.Parametros.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Parametros.Model.Parametro;

public interface ParametroService {
    List<Parametro> findAll();
    Optional<Parametro> findById(Long id);
    Optional<Parametro> findByDescripcion(String descripcion);
    Parametro save(Parametro parametro);
    void delete(Long id);
}
