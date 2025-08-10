package easycommerce.easycommerce.Pago.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Pago.Model.Pago;

public interface PagoService {
    List<Pago> findAll();
    Optional<Pago> findById(Long id);
    Pago save(Pago pago);
    void delete(Long id);
}
