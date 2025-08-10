package easycommerce.easycommerce.Estados.CambioEstado.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.CambioEstado.Service.CambioEstadoService;

@RestController
@CrossOrigin
@RequestMapping("/cambioEstado")
public class CambioEstadoController {

    private final CambioEstadoService cambioEstadoService;

    public CambioEstadoController(CambioEstadoService cambioEstadoService) {
        this.cambioEstadoService = cambioEstadoService;
    }

    @GetMapping
    public ResponseEntity<List<CambioEstado>> getAllCambiosEstado(){
        List<CambioEstado> cambiosEstado = cambioEstadoService.findAll();
        return new ResponseEntity<>(cambiosEstado,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CambioEstado> getCambioEstado(@PathVariable Long id){
        Optional<CambioEstado> cambioEstado = cambioEstadoService.findById(id);
        if(cambioEstado.isPresent()){
            return new ResponseEntity<>(cambioEstado.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<CambioEstado> saveCambioEstado(@RequestBody CambioEstado cambioEstado){
        CambioEstado cambioEstadoGuardado = cambioEstadoService.save(cambioEstado);
        return new ResponseEntity<>(cambioEstadoGuardado,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCambioEstado(@PathVariable Long id){
        Optional<CambioEstado> cambioEstado = cambioEstadoService.findById(id);
        if(cambioEstado.isPresent()){
            cambioEstadoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
