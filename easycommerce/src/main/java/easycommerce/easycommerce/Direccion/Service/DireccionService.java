package easycommerce.easycommerce.Direccion.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Direccion.Model.Direccion;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;

public interface DireccionService {
    List<Direccion> findAll();
    Optional<Direccion> findById(Long id);
    Direccion save(Direccion direccion);
    void delete(Long id);
    List<Direccion> findByCliente(Long id) throws NoSuchElementException;
    Optional<Direccion> verify(String calle, String numero, String ciudad, String codigoPostal, String provincia, String pais, Long idCliente) throws NoSuchElementException;
}
