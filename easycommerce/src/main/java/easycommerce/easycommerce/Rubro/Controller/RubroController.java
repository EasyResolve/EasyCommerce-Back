package easycommerce.easycommerce.Rubro.Controller;

import java.util.List;
import java.util.NoSuchElementException;
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

import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPost;
import easycommerce.easycommerce.Rubro.Model.Rubro;
import easycommerce.easycommerce.Rubro.Service.RubroService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/rubro")
public class RubroController {
    private final RubroService rubroService;

    public RubroController(RubroService rubroService) {
        this.rubroService = rubroService;
    }

    @GetMapping
    public ResponseEntity<List<Rubro>> getAllRubros(){
        List<Rubro> rubros = rubroService.findAll();
        return new ResponseEntity<>(rubros,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rubro> getRubro(@PathVariable Long id) throws NoSuchElementException{
        Optional<Rubro> rubro = rubroService.findById(id);
        if(rubro.isPresent()){
            return new ResponseEntity<>(rubro.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Rubro> getRubroPorCodigo(@PathVariable Long codigo){
        Optional<Rubro> rubro = rubroService.findById(codigo);
        if(rubro.isPresent()){
            return new ResponseEntity<>(rubro.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<List<Rubro>> saveRubros(@Valid @RequestBody List<RubroDTOPost> rubros){
        List<Rubro> rubrosGuardados = rubroService.saveList(rubros);
        return new ResponseEntity<>(rubrosGuardados,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRubro(@PathVariable Long id){
        Optional<Rubro> rubro = rubroService.findById(id);
        if(rubro.isPresent()){
            rubroService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
