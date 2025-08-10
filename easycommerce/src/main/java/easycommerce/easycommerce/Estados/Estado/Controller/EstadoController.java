package easycommerce.easycommerce.Estados.Estado.Controller;

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

import easycommerce.easycommerce.Estados.Estado.Model.Estado;
import easycommerce.easycommerce.Estados.Estado.Service.EstadoService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;

@RestController
@CrossOrigin
@RequestMapping("/estado")
public class EstadoController {
    
    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Estado>> getAllEstados(){
        List<Estado> estados = estadoService.findAll();
        return new ResponseEntity<>(estados,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> getEstado(@PathVariable Long id){
        Optional<Estado> estado = estadoService.findById(id);
        if(estado.isPresent()){
            return new ResponseEntity<>(estado.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Estado> saveEstado(@RequestBody Estado estado) throws EntityAlreadyExistsException{
        Estado estadoGuardado = estadoService.save(estado);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id){
        Optional<Estado> estado = estadoService.findById(id);
        if(estado.isPresent()){
            estadoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
