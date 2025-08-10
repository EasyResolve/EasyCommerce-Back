package easycommerce.easycommerce.CodigoPostalZona.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.CodigoPostalZona.Model.CodigoPostalZona;

public interface CodigoPostalZonaService {
    List<CodigoPostalZona> findAll();
    Optional<CodigoPostalZona> findById(Long id);
    CodigoPostalZona save(CodigoPostalZona codigoPostalZona);
    void delete(Long id);
}
