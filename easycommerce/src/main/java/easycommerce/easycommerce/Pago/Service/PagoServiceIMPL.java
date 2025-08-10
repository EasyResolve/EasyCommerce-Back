package easycommerce.easycommerce.Pago.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import easycommerce.easycommerce.Pago.Model.Pago;
import easycommerce.easycommerce.Pago.Repository.PagoRepository;

@Service
public class PagoServiceIMPL implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceIMPL(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pago save(Pago pago) {
        pago = pagoRepository.save(pago);
        return pago;
    }

    @Override
    public void delete(Long id) {
        pagoRepository.deleteById(id);
    } 
}
