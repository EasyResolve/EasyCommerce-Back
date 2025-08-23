package easycommerce.easycommerce.BloqueLibre.Controller;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import easycommerce.easycommerce.BloqueLibre.Models.BloqueLibre;
import easycommerce.easycommerce.BloqueLibre.Service.BloqueLibreService;

@RestController
@CrossOrigin
@RequestMapping("/bloqueLibre")
public class BloqueLibreController {

    private BloqueLibreService bloqueLibreService;

    public BloqueLibreController(BloqueLibreService bloqueLibreService) {
        this.bloqueLibreService = bloqueLibreService;
    }
    
    @GetMapping
    public ResponseEntity<List<BloqueLibre>> findAllBloqueLibre(){
        List<BloqueLibre> bloquesLibres = bloqueLibreService.findAll();
        return new ResponseEntity<>(bloquesLibres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloqueLibre> findBloqueLibre(@PathVariable Long id){
        Optional<BloqueLibre> bloque = bloqueLibreService.findById(id);
        if(bloque.isPresent()){
            return new ResponseEntity<>(bloque.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<BloqueLibre> saveBloqueLibre(@RequestBody BloqueLibre bloqueLibre){
        BloqueLibre savedBloque = bloqueLibreService.save(bloqueLibre);
        return new ResponseEntity<>(bloqueLibre, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloqueLibre> updateBloqueLibre(@PathVariable Long id, @RequestBody BloqueLibre bloqueLibre){
        Optional<BloqueLibre> bloque = bloqueLibreService.findById(id);
        if(bloque.isPresent()){
            BloqueLibre updateBloque = bloque.get();

            if(bloqueLibre.getDescripcion() != null){
               updateBloque.setDescripcion(bloqueLibre.getDescripcion()); 
            }
            if(bloqueLibre.getOrden() != null){
                updateBloque.setOrden(bloqueLibre.getOrden());
            }
            if(bloqueLibre.getTipoBloque() != null){
                updateBloque.setTipoBloque(bloqueLibre.getTipoBloque());
            }
            if(bloqueLibre.getTipoFiltro() != null){
                updateBloque.setTipoFiltro(bloqueLibre.getTipoFiltro());
            }
            if(bloqueLibre.getTitulo() != null){
                updateBloque.setTitulo(bloqueLibre.getTitulo());
            }

            updateBloque = bloqueLibreService.save(updateBloque);
            return new ResponseEntity<>(updateBloque, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloqueLibre(@PathVariable Long id){
        Optional<BloqueLibre> bloque = bloqueLibreService.findById(id);
        if(bloque.isPresent()){
            bloqueLibreService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
