package easycommerce.easycommerce.Caja.Controller;

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

import easycommerce.easycommerce.Caja.Model.Caja;
import easycommerce.easycommerce.Caja.Service.CajaService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;


@RestController
@CrossOrigin
@RequestMapping("/caja")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping
    public ResponseEntity<List<Caja>> getAllCajas(){
        List<Caja> cajas = cajaService.findAll();
        return new ResponseEntity<>(cajas,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caja> getCaja(@PathVariable Long id){
        Optional<Caja> caja = cajaService.findById(id);
        if(caja.isPresent()){
            return new ResponseEntity<>(caja.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Caja> saveCaja(@RequestBody Caja caja) throws EntityAlreadyExistsException{
        caja.setPeso(20000.00);
        Caja cajaGuardada = cajaService.save(caja);
        return new ResponseEntity<>(cajaGuardada,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaja(@PathVariable Long id){
        Optional<Caja> caja = cajaService.findById(id);
        if(caja.isPresent()){
            cajaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}   
