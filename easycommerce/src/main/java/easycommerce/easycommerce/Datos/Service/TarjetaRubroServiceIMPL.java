package easycommerce.easycommerce.Datos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Datos.Model.TarjetaRubro;
import easycommerce.easycommerce.Datos.Repository.TarjetaRubroRepository;

@Service
public class TarjetaRubroServiceIMPL implements TarjetaRubroService {

    private final TarjetaRubroRepository tarjetaRubroRepository;

    public TarjetaRubroServiceIMPL(TarjetaRubroRepository tarjetaRubroRepository) {
        this.tarjetaRubroRepository = tarjetaRubroRepository;
    }

    @Override
    public List<TarjetaRubro> findAll() {
        return tarjetaRubroRepository.findAll();
    }

    @Override
    public Optional<TarjetaRubro> findById(Long id) {
        return tarjetaRubroRepository.findById(id);
    }

    @Override
    public TarjetaRubro save(TarjetaRubro tarjetaRubro) {
        return tarjetaRubroRepository.save(tarjetaRubro);
    }

    @Override
    public void delete(Long id) {
        tarjetaRubroRepository.deleteById(id);
    }

    
}
