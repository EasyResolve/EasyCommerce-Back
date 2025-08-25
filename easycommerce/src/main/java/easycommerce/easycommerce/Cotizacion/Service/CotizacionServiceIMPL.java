package easycommerce.easycommerce.Cotizacion.Service;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Cotizacion.Repository.CotizacionRepository;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;
import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;

@Service
public class CotizacionServiceIMPL implements CotizacionService {
    private final CotizacionRepository cotizacionRepository;
    private final ParametroRepository parametroRepository;

    public CotizacionServiceIMPL(CotizacionRepository cotizacionRepository, ParametroRepository parametroRepository) {
        this.cotizacionRepository = cotizacionRepository;
        this.parametroRepository = parametroRepository;
    }

    @Override
    public Optional<Cotizacion> findByMoneda(String moneda) {
        return cotizacionRepository.findByMoneda(moneda);
    }

    @Override
    public Cotizacion save(Cotizacion cotizacion) {
        return cotizacionRepository.save(cotizacion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cotizacion obtenerCotizacion(String moneda) throws QuotationNotFoundException, IOException {
        String url = "https://www.bna.com.ar/Personas";
        
        Document doc = Jsoup.connect(url).get();
        
        Elements tablaCotizacion = doc.select("table.cotizacion");
        
        if (!tablaCotizacion.isEmpty()) {
                        
            Element fila = tablaCotizacion.select("tr").get(1);
        
            Elements celdas = fila.select("td");
        
            String tipoDolar = celdas.get(0).text();
            String compra = celdas.get(1).text();
            String venta = celdas.get(2).text();
                        
            Optional<Cotizacion> cotizacionGuardada = cotizacionRepository.findByMoneda(moneda);
            if(cotizacionGuardada.isPresent()){
                Cotizacion cotizacionBd = cotizacionGuardada.get();
                BigDecimal precioCompra = BigDecimal.valueOf(Double.parseDouble(compra.replace(",", ".")));
                BigDecimal precioVenta = BigDecimal.valueOf(Double.parseDouble(venta.replace(",", ".")));
                BigDecimal diferencia = cotizacionBd.getPrecioVenta().subtract(precioVenta).abs();
                Double porcentajeDiferencia = diferencia.divide(precioVenta).doubleValue();
                Optional<Parametro> porcentajeVariacionPermitida = parametroRepository.findByDescripcion("PorcentajeVariacionDolar");
                if(cotizacionBd.getPrecioVenta().compareTo(precioVenta) < 0 && porcentajeDiferencia < Double.parseDouble(porcentajeVariacionPermitida.get().getValor().toString())){
                    cotizacionBd.setPrecioCompra(precioCompra.setScale(2,RoundingMode.HALF_UP));
                    cotizacionBd.setPrecioVenta(precioVenta.setScale(2,RoundingMode.HALF_UP));
                    cotizacionBd.setFechaUltimaActualizacion(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                }
                return cotizacionRepository.save(cotizacionBd);
            }
            else{
                Cotizacion cotizacionBd = new Cotizacion();
                cotizacionBd.setMoneda("USD");
                BigDecimal precioCompra = BigDecimal.valueOf(Double.parseDouble(compra.replace(",", ".")));
                cotizacionBd.setPrecioCompra(precioCompra.setScale(2, RoundingMode.HALF_UP));
                BigDecimal precioVenta = BigDecimal.valueOf(Double.parseDouble(venta.replace(",", ".")));
                cotizacionBd.setPrecioVenta(precioVenta.setScale(2, RoundingMode.HALF_UP));
                cotizacionBd.setFechaUltimaActualizacion(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                return cotizacionRepository.save(cotizacionBd);
            }
        }
        else{
            throw new QuotationNotFoundException("No se puede actualizar el valor del dolar");
        }
    }       
}
