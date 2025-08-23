package easycommerce.easycommerce.BloqueLibre.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.BloqueLibre.Models.BloqueLibre;
import easycommerce.easycommerce.BloqueLibre.Repository.BloqueLibreRepository;

@Service
public class BloqueLibreServiceIMPL implements BloqueLibreService{
    private BloqueLibreRepository bloqueLibreRepository;

    public BloqueLibreServiceIMPL(BloqueLibreRepository bloqueLibreRepository) {
        this.bloqueLibreRepository = bloqueLibreRepository;
    }

    @Override
    public List<BloqueLibre> findAll() {
        return bloqueLibreRepository.findAll();
    }

    @Override
    public Optional<BloqueLibre> findById(Long id) {
        return bloqueLibreRepository.findById(id);
    }

    @Override
    public BloqueLibre save(BloqueLibre bloqueLibre) {
        return bloqueLibreRepository.save(bloqueLibre);
    }

    @Override
    public void delete(Long id) {
        bloqueLibreRepository.deleteById(id);
    }
}
