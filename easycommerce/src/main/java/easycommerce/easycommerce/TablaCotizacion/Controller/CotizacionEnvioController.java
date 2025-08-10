package easycommerce.easycommerce.TablaCotizacion.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;
import easycommerce.easycommerce.TablaCotizacion.Service.CotizacionEnvioService;

@RestController
@CrossOrigin
@RequestMapping("/cotizacionEnvio")
public class CotizacionEnvioController {
    private final CotizacionEnvioService cotizacionEnvioService;

    public CotizacionEnvioController(CotizacionEnvioService cotizacionEnvioService) {
        this.cotizacionEnvioService = cotizacionEnvioService;
    }

    @GetMapping
    public ResponseEntity<List<CotizacionEnvio>> getAllCotizacionesEnvio(){
        List<CotizacionEnvio> cotizaciones = cotizacionEnvioService.findAll();
        return new ResponseEntity<>(cotizaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionEnvio> getCotizacionEnvio(@PathVariable Long id){
        Optional<CotizacionEnvio> cotizacionBd = cotizacionEnvioService.findById(id);
        if(cotizacionBd.isPresent()){
            return new ResponseEntity<>(cotizacionBd.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<CotizacionEnvio> saveCotizacionEnvio(@RequestBody CotizacionEnvio cotizacionEnvio){
        CotizacionEnvio cotizacionGuardada = cotizacionEnvioService.save(cotizacionEnvio);
        return new ResponseEntity<>(cotizacionGuardada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CotizacionEnvio> updateCotizacionEnvio(@PathVariable Long id, @RequestBody CotizacionEnvio cotizacionEnvio){
        Optional<CotizacionEnvio> cotizacionBd = cotizacionEnvioService.findById(id);
        if(cotizacionBd.isPresent()){
            CotizacionEnvio cotizacionAModificar = cotizacionBd.get();

            if(cotizacionEnvio.getMontoEnvio() != null){
                cotizacionAModificar.setMontoEnvio(cotizacionEnvio.getMontoEnvio());
            }

            if(cotizacionEnvio.getPesoDesde() != null){
                cotizacionAModificar.setPesoDesde(cotizacionEnvio.getPesoDesde());
            }

            if(cotizacionEnvio.getPesoHasta() != null){
                cotizacionAModificar.setPesoHasta(cotizacionEnvio.getPesoHasta());
            }

            cotizacionAModificar = cotizacionEnvioService.save(cotizacionAModificar);
            return new ResponseEntity<>(cotizacionAModificar, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCotizacionEnvio(@PathVariable Long id){
        Optional<CotizacionEnvio> cotizacionBd = cotizacionEnvioService.findById(id);
        if(cotizacionBd.isPresent()){
            cotizacionEnvioService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
