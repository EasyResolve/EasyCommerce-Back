package easycommerce.easycommerce.Datos.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Datos.Model.ImagenCarrusel;
import easycommerce.easycommerce.Datos.Model.ImagenCarruselDTOPost;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;

public interface ImagenCarruselService {
    List<ImagenCarrusel> findAll();
    Optional<ImagenCarrusel> findById(Long id);
    ImagenCarrusel save(ImagenCarruselDTOPost imagenCarrusel) throws NoSuchElementException, IOException;
    void delete(Long id) throws IOException;
}
