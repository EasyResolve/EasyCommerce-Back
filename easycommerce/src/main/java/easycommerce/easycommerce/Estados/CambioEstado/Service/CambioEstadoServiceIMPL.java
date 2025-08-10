package easycommerce.easycommerce.Estados.CambioEstado.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.CambioEstado.Repository.CambioEstadoRepository;

@Service
public class CambioEstadoServiceIMPL implements CambioEstadoService {

    private final CambioEstadoRepository cambioEstadoRepository;

    public CambioEstadoServiceIMPL(CambioEstadoRepository cambioEstadoRepository) {
        this.cambioEstadoRepository = cambioEstadoRepository;
    }

    @Override
    public List<CambioEstado> findAll() {
        return cambioEstadoRepository.findAll();
    }

    @Override
    public Optional<CambioEstado> findById(Long id) {
       return cambioEstadoRepository.findById(id);
    }

    @Override
    public CambioEstado save(CambioEstado cambioEstado) {
        return cambioEstadoRepository.save(cambioEstado);
    }

    @Override
    public void delete(Long id) {
        cambioEstadoRepository.deleteById(id);
    }

    
}
