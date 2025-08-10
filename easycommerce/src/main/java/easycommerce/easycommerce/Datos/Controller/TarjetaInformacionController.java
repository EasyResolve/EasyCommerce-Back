package easycommerce.easycommerce.Datos.Controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Datos.Model.TarjetaInformacion;
import easycommerce.easycommerce.Datos.Model.TarjetaInformacionDTOPost;
import easycommerce.easycommerce.Datos.Service.TarjetaInformacionService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;



@RestController
@CrossOrigin
@RequestMapping("/tarjetaInformacion")
public class TarjetaInformacionController {

    private final TarjetaInformacionService tarjetaInformacionService;

    public TarjetaInformacionController(TarjetaInformacionService tarjetaInformacionService) {
        this.tarjetaInformacionService = tarjetaInformacionService;
    }

    @GetMapping
    public ResponseEntity<List<TarjetaInformacion>> getAllTarjetasInformacion(){
        List<TarjetaInformacion> tarjetas = tarjetaInformacionService.findAll();
        return new ResponseEntity<>(tarjetas,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarjetaInformacion> getTarjetaInformacion(@PathVariable Long id) throws NoSuchElementException{
        Optional<TarjetaInformacion> tajetaBd = tarjetaInformacionService.findById(id);
        if(tajetaBd.isPresent()){
            return new ResponseEntity<>(tajetaBd.get(),HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentra la tarjeta con id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<TarjetaInformacion> saveTarjetaInformacion(@RequestParam String texto, @RequestParam(required = false) String icono, @RequestParam(required = false) MultipartFile file) throws NoSuchElementException, IOException{
        TarjetaInformacion tarjeta = TarjetaInformacion.builder().icono(icono).texto(texto).build();
        TarjetaInformacionDTOPost tarjetaAGuardar = new TarjetaInformacionDTOPost(tarjeta, file);
        TarjetaInformacion tarjetaGuardada = tarjetaInformacionService.save(tarjetaAGuardar);  
        return new ResponseEntity<>(tarjetaGuardada,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarjetaInformacion> updateTarjetaInformacion(@PathVariable Long id, @RequestParam String texto, @RequestParam(required = false) String icono, @RequestParam(required = false) MultipartFile file) throws NoSuchElementException, IOException{
        TarjetaInformacion tarjeta = TarjetaInformacion.builder().id(id).icono(icono).texto(texto).build();
        TarjetaInformacionDTOPost tarjetaAGuardar = new TarjetaInformacionDTOPost(tarjeta, file);
        TarjetaInformacion tarjetaGuardada = tarjetaInformacionService.save(tarjetaAGuardar);
        return new ResponseEntity<>(tarjetaGuardada,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarjetaInformacion(@PathVariable Long id) throws NoSuchElementException, IOException{
        Optional<TarjetaInformacion> tarjetaBd = tarjetaInformacionService.findById(id);
        if(tarjetaBd.isPresent()){
            tarjetaInformacionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            throw new NoSuchElementException("No se encuentra la tarjeta con id: " + id);
        }
    }
    
}
