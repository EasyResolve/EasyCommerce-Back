package easycommerce.easycommerce.Rubro.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPost;
import easycommerce.easycommerce.Rubro.Model.Rubro;


public interface RubroService {
    List<Rubro> findAll();
    Optional<Rubro> findById(Long id);
    List<Rubro> saveList(List<RubroDTOPost> rubros);
    Rubro save(Rubro rubro);
    void delete(Long id);
}
