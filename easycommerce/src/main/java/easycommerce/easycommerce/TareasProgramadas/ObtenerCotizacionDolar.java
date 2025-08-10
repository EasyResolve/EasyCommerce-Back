package easycommerce.easycommerce.TareasProgramadas;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Cotizacion.Service.CotizacionService;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;


@Component
public class ObtenerCotizacionDolar {
    private final CotizacionService cotizacionService;
    
    public ObtenerCotizacionDolar(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @Scheduled(cron = "0 0/30 * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void obtenerCotizacionDolar() throws QuotationNotFoundException, IOException{
        Cotizacion cotizacion = cotizacionService.obtenerCotizacion("USD");
    }
}
