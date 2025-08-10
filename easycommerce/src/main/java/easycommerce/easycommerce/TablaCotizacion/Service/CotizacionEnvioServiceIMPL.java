package easycommerce.easycommerce.TablaCotizacion.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;
import easycommerce.easycommerce.TablaCotizacion.Repository.CotizacionEnvioRepository;



@Service
public class CotizacionEnvioServiceIMPL implements CotizacionEnvioService{

    private final CotizacionEnvioRepository cotizacionEnvioRepository;

    public CotizacionEnvioServiceIMPL(CotizacionEnvioRepository cotizacionEnvioRepository) {
        this.cotizacionEnvioRepository = cotizacionEnvioRepository;
    }

    @Override
    public List<CotizacionEnvio> findAll() {
        return cotizacionEnvioRepository.findAll();
    }

    @Override
    public Optional<CotizacionEnvio> findById(Long id) {
        return cotizacionEnvioRepository.findById(id);
    }

    @Override
    public Optional<CotizacionEnvio> findByEntreValores(Double peso) {
        return cotizacionEnvioRepository.findByEntreValores(peso);
    }

    @Override
    public CotizacionEnvio save(CotizacionEnvio cotizacionEnvio) {
        return cotizacionEnvioRepository.save(cotizacionEnvio);
    }

    @Override
    public void delete(Long id) {
        cotizacionEnvioRepository.deleteById(id);
    }

    
}
