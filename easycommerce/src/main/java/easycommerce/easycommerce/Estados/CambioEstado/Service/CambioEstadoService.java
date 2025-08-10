package easycommerce.easycommerce.Estados.CambioEstado.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;

public interface CambioEstadoService {
    List<CambioEstado> findAll();
    Optional<CambioEstado> findById(Long id);
    CambioEstado save(CambioEstado cambioEstado);
    void delete(Long id);
}
