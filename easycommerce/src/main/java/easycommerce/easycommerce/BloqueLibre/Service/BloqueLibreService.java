package easycommerce.easycommerce.BloqueLibre.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.BloqueLibre.Models.BloqueLibre;

public interface BloqueLibreService {
    List<BloqueLibre> findAll();
    Optional<BloqueLibre> findById(Long id);
    BloqueLibre save(BloqueLibre bloqueLibre);
    void delete(Long id);
}
