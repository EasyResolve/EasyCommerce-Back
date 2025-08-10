package easycommerce.easycommerce.Datos.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Datos.Model.TarjetaInformacion;
import easycommerce.easycommerce.Datos.Model.TarjetaInformacionDTOPost;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;

public interface TarjetaInformacionService {
    List<TarjetaInformacion> findAll();
    Optional<TarjetaInformacion> findById(Long id);
    TarjetaInformacion save(TarjetaInformacionDTOPost tarjetaInformacion) throws NoSuchElementException, IOException;
    void delete(Long id) throws IOException;
}
