package easycommerce.easycommerce.CodigoPostalZona.Controller;

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

import easycommerce.easycommerce.CodigoPostalZona.Model.CodigoPostalZona;
import easycommerce.easycommerce.CodigoPostalZona.Service.CodigoPostalZonaService;

@RestController
@CrossOrigin
@RequestMapping("/codigoPostalZona")
public class CodigoPostalZonaController {
    
    private final CodigoPostalZonaService codigoPostalZonaService;

    public CodigoPostalZonaController(CodigoPostalZonaService codigoPostalZonaService) {
        this.codigoPostalZonaService = codigoPostalZonaService;
    }
    
    @GetMapping
    public ResponseEntity<List<CodigoPostalZona>> getAllCodigosPostalesZona(){
        List<CodigoPostalZona> codigos = codigoPostalZonaService.findAll();
        return new ResponseEntity<>(codigos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodigoPostalZona> getCodigoPostalZona(@PathVariable Long id){
        Optional<CodigoPostalZona> codigo = codigoPostalZonaService.findById(id);
        if(codigo.isPresent()){
            return new ResponseEntity<>(codigo.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CodigoPostalZona> saveCodigoPostal(@RequestBody CodigoPostalZona codigoPostalZona){
        CodigoPostalZona codigoPostalGuardado = codigoPostalZonaService.save(codigoPostalZona);
        return new ResponseEntity<>(codigoPostalGuardado,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CodigoPostalZona> updateCodigoPostal(@PathVariable Long id, @ RequestBody CodigoPostalZona codigoPostalZona){
        Optional<CodigoPostalZona> codigoBd = codigoPostalZonaService.findById(id);
        if(codigoBd.isPresent()){
            CodigoPostalZona codigoPostal = codigoBd.get();
            if(codigoPostalZona.getCiudad() != null){
                codigoPostal.setCiudad(codigoPostalZona.getCiudad());
            }
            if(codigoPostalZona.getCodigoPostal() != null){
                codigoPostal.setCodigoPostal(codigoPostalZona.getCodigoPostal());
            }
            codigoPostal = codigoPostalZonaService.save(codigoPostal);
            return new ResponseEntity<>(codigoPostal,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodigoPostal(@PathVariable Long id){
        Optional<CodigoPostalZona> codigoBd = codigoPostalZonaService.findById(id);
        if(codigoBd.isPresent()){
            codigoPostalZonaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
