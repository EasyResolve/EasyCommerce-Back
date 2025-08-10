package easycommerce.easycommerce.CodigoPostalZona.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.CodigoPostalZona.Model.CodigoPostalZona;
import easycommerce.easycommerce.CodigoPostalZona.Repository.CodigoPostalZonaRepository;

@Service
public class CodigoPostalZonaServiceIMPL implements CodigoPostalZonaService {

    private final CodigoPostalZonaRepository codigoPostalZonaRepository;

    public CodigoPostalZonaServiceIMPL(CodigoPostalZonaRepository codigoPostalZonaRepository) {
        this.codigoPostalZonaRepository = codigoPostalZonaRepository;
    }

    @Override
    public List<CodigoPostalZona> findAll() {
        return codigoPostalZonaRepository.findAll();
    }

    @Override
    public Optional<CodigoPostalZona> findById(Long id) {
        return codigoPostalZonaRepository.findById(id);
    }

    @Override
    public CodigoPostalZona save(CodigoPostalZona codigoPostalZona) {
        return codigoPostalZonaRepository.save(codigoPostalZona);
    }

    @Override
    public void delete(Long id) {
        codigoPostalZonaRepository.deleteById(id);
    }

    
}
