package easycommerce.easycommerce.Rubro.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPost;
import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPut;
import easycommerce.easycommerce.Rubro.Model.Rubro;


public interface RubroService {
    List<Rubro> findAll();
    Optional<Rubro> findById(Long id);
    List<Rubro> saveList(List<RubroDTOPost> rubros) throws Exception;
    Rubro save(RubroDTOPut rubro) throws IOException;
    void delete(Long id);
}
