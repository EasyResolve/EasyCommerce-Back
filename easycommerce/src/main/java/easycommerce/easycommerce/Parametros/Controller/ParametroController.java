package easycommerce.easycommerce.Parametros.Controller;

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

import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Service.ParametroService;

@RestController
@CrossOrigin
@RequestMapping("/parametro")
public class ParametroController {
    
    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @GetMapping
    public ResponseEntity<List<Parametro>> getAllParametros(){
        List<Parametro> parametros = parametroService.findAll();
        return new ResponseEntity<>(parametros,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parametro> getParametroById(@PathVariable Long id){
        Optional<Parametro> parametro = parametroService.findById(id);
        if(parametro.isPresent()){
            return new ResponseEntity<>(parametro.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/descripcion/{descripcion}")
    public ResponseEntity<Parametro> getParametroByDescripcion(@PathVariable String descripcion){
        Optional<Parametro> parametro = parametroService.findByDescripcion(descripcion);
        if(parametro.isPresent()){
            return new ResponseEntity<>(parametro.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Parametro> saveParametro(@RequestBody Parametro parametro){
        Parametro parametroGuardado = parametroService.save(parametro);
        return new ResponseEntity<>(parametroGuardado,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParametro(@PathVariable Long id){
        Optional<Parametro> parametro = parametroService.findById(id);
        if(parametro.isPresent()){
            parametroService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
