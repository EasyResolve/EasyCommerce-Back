package easycommerce.easycommerce.Caja.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Caja.Model.Caja;
import easycommerce.easycommerce.Caja.Repository.CajaRepository;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;


@Service
public class CajaServiceIMPL implements CajaService {

    private final CajaRepository cajaRepository;

    public CajaServiceIMPL(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Override
    public List<Caja> findAll() {
        return cajaRepository.findAll();
    }

    @Override
    public Optional<Caja> findById(Long id) {
        return cajaRepository.findById(id);
    }

    @Override
    public Caja save(Caja caja) throws EntityAlreadyExistsException {
        Optional<Caja> cajaBd = cajaRepository.findByDescripcion(caja.getDescripcion());
        if(!cajaBd.isPresent()){
            return cajaRepository.save(caja);
        }
        else{
            throw new EntityAlreadyExistsException("Ya se encuentra guardada una caja con la descripcion: " + caja.getDescripcion());
        }
        
    }

    @Override
    public void delete(Long id) {
        cajaRepository.deleteById(id);
    }

    
}
