package easycommerce.easycommerce.Datos.Controller;

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

import easycommerce.easycommerce.Datos.Model.TarjetaRubro;
import easycommerce.easycommerce.Datos.Service.TarjetaRubroService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;


@RestController
@CrossOrigin
@RequestMapping("/tarjetaRubro")
public class TarjetaRubroController {

    private final TarjetaRubroService tarjetaRubroService;

    public TarjetaRubroController(TarjetaRubroService tarjetaRubroService) {
        this.tarjetaRubroService = tarjetaRubroService;
    }

    @GetMapping
    public ResponseEntity<List<TarjetaRubro>> getAllTarjetasRubro(){
        List<TarjetaRubro> tarjetas = tarjetaRubroService.findAll();
        return new ResponseEntity<>(tarjetas,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarjetaRubro> getTarjetaRubro(@PathVariable Long id) throws NoSuchElementException{
        Optional<TarjetaRubro> tarjetaBd = tarjetaRubroService.findById(id);
        if(tarjetaBd.isPresent()){
            return new ResponseEntity<>(tarjetaBd.get(),HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentra la tarjeta con id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<TarjetaRubro> saveTarjetaRubro(@RequestBody TarjetaRubro tarjetaRubro){
        TarjetaRubro tarjetaAGuardar = tarjetaRubroService.save(tarjetaRubro);
        return new ResponseEntity<>(tarjetaAGuardar,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarjetaRubro> updateTarjetaRubro(@PathVariable Long id, @RequestBody TarjetaRubro tarjetaRubro) throws NoSuchElementException{
        Optional<TarjetaRubro> tajetaRubroBd = tarjetaRubroService.findById(id);
        if(tajetaRubroBd.isPresent()){
            TarjetaRubro tarjetaAGuardar = tajetaRubroBd.get();
            if(tarjetaRubro.getIcono() != null){
                tarjetaAGuardar.setIcono(tarjetaRubro.getIcono());
            }
            if(tarjetaRubro.getRubro() != null){
                tarjetaAGuardar.setRubro(tarjetaRubro.getRubro());
            }
            tarjetaAGuardar = tarjetaRubroService.save(tarjetaRubro);
            return new ResponseEntity<>(tarjetaAGuardar,HttpStatus.CREATED);
        }
        else{
            throw new NoSuchElementException("No se encuentra la tarjeta con id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarjetaRubro(@PathVariable Long id) throws NoSuchElementException{
        Optional<TarjetaRubro> targetaBd = tarjetaRubroService.findById(id);
        if(targetaBd.isPresent()){
            tarjetaRubroService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            throw new NoSuchElementException("No se encuentra la tarjeta con id: " + id);
        }
    }
}
