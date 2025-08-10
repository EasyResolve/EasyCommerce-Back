package easycommerce.easycommerce.TablaCotizacion.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;


public interface CotizacionEnvioService{
    List<CotizacionEnvio> findAll();
    Optional<CotizacionEnvio> findById(Long id);
    Optional<CotizacionEnvio> findByEntreValores(Double peso);
    CotizacionEnvio save(CotizacionEnvio cotizacionEnvio);
    void delete(Long id);
}
