package easycommerce.easycommerce.Cotizacion.Service;

import java.io.IOException;
import java.util.Optional;

import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;

public interface CotizacionService {
    Optional<Cotizacion> findByMoneda(String moneda);
    Cotizacion save(Cotizacion cotizacion);
    Cotizacion obtenerCotizacion(String moneda) throws QuotationNotFoundException, IOException;
}
