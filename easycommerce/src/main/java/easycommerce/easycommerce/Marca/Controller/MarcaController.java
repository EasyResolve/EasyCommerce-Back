package easycommerce.easycommerce.Marca.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Marca.DTOs.MarcaDTOPost;
import easycommerce.easycommerce.Marca.Service.MarcaService;

@RestController
@CrossOrigin
@RequestMapping("/marca")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<Marca>> getAllMarcas(){
        List<Marca> marcas = marcaService.findAll();
        return new ResponseEntity<>(marcas,HttpStatus.OK);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Marca> getMarca(@PathVariable Long codigo){
        Optional<Marca> marca = marcaService.findByCodigo(codigo);
        if(marca.isPresent()){
            return new ResponseEntity<>(marca.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/lista")
    public ResponseEntity<List<Marca>> saveMarcas(@RequestBody List<Marca> marcas){
        List<Marca> marcasGuardadas = marcaService.saveLista(marcas);
        return new ResponseEntity<>(marcasGuardadas,HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Marca> saveMarca(@RequestParam String  nombre, @RequestParam Boolean  mostrar ,@RequestParam(required = false) MultipartFile  file) throws IOException{
        MarcaDTOPost marcaDTO = new MarcaDTOPost(Marca.builder().descripcion(nombre).mostrar(mostrar).build(), file);
        Marca marcaGuardada = marcaService.saveMarca(marcaDTO);
        return new ResponseEntity<>(marcaGuardada,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(@PathVariable Long id, @RequestParam String descripcion, @RequestParam(required = false) String url, @RequestParam boolean mostrar, @RequestParam(required = false) MultipartFile file ) throws IOException{
        Marca marca = Marca.builder().codigo(id).descripcion(descripcion).mostrar(mostrar).urlImagen(url).build();
        Optional<Marca> marcaBd = marcaService.findByCodigo(id);
        if(marcaBd.isPresent()){
            Marca marcaAGuardar = marcaBd.get();
            if(marca.getDescripcion() != null){
                marcaAGuardar.setDescripcion(marca.getDescripcion());
            }
            if(marca.getUrlImagen() != null){
                marcaAGuardar.setUrlImagen(marca.getUrlImagen());
            }
            if(marca.isMostrar()){
                marcaAGuardar.setMostrar(true);
            }
            else{
                marcaAGuardar.setMostrar(false);
            }
            MarcaDTOPost marcaNueva = new MarcaDTOPost(marcaAGuardar, file);
            marcaAGuardar = marcaService.saveMarca(marcaNueva);
            return new ResponseEntity<>(marcaAGuardar,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Long codigo) throws IOException{
        Optional<Marca> marca = marcaService.findByCodigo(codigo);
        if(marca.isPresent()){
            marcaService.delete(codigo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
