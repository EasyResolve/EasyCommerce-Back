package easycommerce.easycommerce.Datos.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Datos.Model.TarjetaRubro;

public interface TarjetaRubroService {
    List<TarjetaRubro> findAll();
    Optional<TarjetaRubro> findById(Long id);
    TarjetaRubro save(TarjetaRubro tarjetaRubro);
    void delete(Long id);
}
