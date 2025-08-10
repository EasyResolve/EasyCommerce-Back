package easycommerce.easycommerce.Marca.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Marca.DTOs.MarcaDTOPost;
import easycommerce.easycommerce.Marca.Model.Marca;

public interface MarcaService {
    List<Marca> findAll();
    Optional<Marca> findByCodigo(Long codigo);
    List<Marca> saveLista(List<Marca> marcas);
    void delete(Long codigo) throws IOException;
    Marca saveMarca(MarcaDTOPost marca) throws IOException;
}
