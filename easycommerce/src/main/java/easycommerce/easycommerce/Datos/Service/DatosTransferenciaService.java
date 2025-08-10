package easycommerce.easycommerce.Datos.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Datos.Model.DatosTransferencia;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;

public interface DatosTransferenciaService {
    List<DatosTransferencia> findAll();
    Optional<DatosTransferencia> findById(Long id);
    DatosTransferencia save(DatosTransferencia datosTransferencia);
    void delete(Long id) throws NoSuchElementException;
}
