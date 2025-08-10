package easycommerce.easycommerce.Estados.Estado.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Estados.Estado.Model.Estado;
import easycommerce.easycommerce.Estados.Estado.Repository.EstadoRepository;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;

@Service
public class EstadoServiceIMPL implements EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoServiceIMPL(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @Override
    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    @Override
    public Optional<Estado> findById(Long id) {
       return estadoRepository.findById(id);
    }

    @Override
    public Estado save(Estado estado) throws EntityAlreadyExistsException {
        Optional<Estado> estadoBd = estadoRepository.findByDescripcion(estado.getDescripcion());
        if(!estadoBd.isPresent()){
            return estadoRepository.save(estado);
        }
        else{
            throw new EntityAlreadyExistsException("Ya existe un estado con la descripcion: " + estado.getDescripcion());
        }
        
    }

    @Override
    public void delete(Long id) {
        estadoRepository.deleteById(id);
    }

    
}
