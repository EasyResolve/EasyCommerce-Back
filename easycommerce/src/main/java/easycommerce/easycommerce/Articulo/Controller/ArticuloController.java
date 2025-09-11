package easycommerce.easycommerce.Articulo.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOActualizacion;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOGet;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTONombre;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOPost;
import easycommerce.easycommerce.Articulo.DTOs.IsDestacado;
import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Articulo.Model.ArticuloConImagenesYErrores;
import easycommerce.easycommerce.Articulo.Service.ArticuloService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;



@RestController
@CrossOrigin
@RequestMapping("/articulo")
public class ArticuloController {
    private final ArticuloService articuloService;

    
    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }


    @GetMapping
    public ResponseEntity<List<ArticuloDTOGet>> getAllArticulos() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        List<ArticuloDTOGet> articulos = articuloService.findAll(username);
        return new ResponseEntity<>(articulos,HttpStatus.OK);
    }

    @GetMapping("/sinStock")
    public ResponseEntity<List<ArticuloDTOGet>> getArticuloSinStock() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        List<ArticuloDTOGet> articulos = articuloService.findAriticulosSinStock(username);
        return new ResponseEntity<>(articulos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDTOGet> getArticulo(@PathVariable Long id) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        Optional<ArticuloDTOGet> articulo = articuloService.findById(id, username);
        if(articulo.isPresent()){
            ArticuloDTOGet articuloBd = articulo.get();
            return new ResponseEntity<>(articuloBd,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*@GetMapping("/rubro/{codigo}")
    public ResponseEntity<List<ArticuloDTOGet>> getAllArticulosPorRubro(@PathVariable Long codigo) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        List<ArticuloDTOGet> articulos = articuloService.findByRubro(codigo, username);
        return new ResponseEntity<>(articulos,HttpStatus.OK);
    }*/

    @PostMapping("/nombre")
    public ResponseEntity<ArticuloDTOGet> getArticuloByNombre(@RequestBody ArticuloDTONombre nombre) throws NoSuchElementException, Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        Optional<ArticuloDTOGet> articulo = articuloService.findByNombre(nombre.nombre(), username);
        if(articulo.isPresent()){
            ArticuloDTOGet articuloAMostrar = articulo.get();
            return new ResponseEntity<>(articuloAMostrar,HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentra el articulo con nombre: " + nombre);
        }
    }

    @PostMapping("/actualizacion")
    public ResponseEntity<List<ArticuloDTOGet>> actualizarPrecioArticulos(@RequestBody ArticuloDTOActualizacion articulos) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        List<ArticuloDTOGet> articulosActualizados = articuloService.actualizarPrecioArticulos(articulos, username);
        return new ResponseEntity<>(articulosActualizados,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<Articulo>> saveArticulos(@RequestBody List<ArticuloDTOPost> articulos) throws Exception{
        List<Articulo> articulosGuardados = articuloService.save(articulos);
        return new ResponseEntity<>(articulosGuardados,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticulo(@PathVariable Long id) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        Optional<ArticuloDTOGet> articulo = articuloService.findById(id, username);
        if(articulo.isPresent()){
            articuloService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cargarImagenes")
    public ResponseEntity<Void> cargarImagenesMasivas(@RequestParam List<MultipartFile> imagenes) throws NoSuchElementException, IOException{
        articuloService.procesarImagenesAsync(imagenes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/eliminarImagenes")
    public ResponseEntity<List<String>> eliminarImagenes(@RequestBody List<String> imagenesAEliminar) throws IOException{
        List<String> imagenesEliminadas = articuloService.borrarImagenes(imagenesAEliminar);
        return new ResponseEntity<>(imagenesEliminadas, HttpStatus.OK);
    }

    @PutMapping("/Articulo/Consulta")
    public ResponseEntity<ArticuloDTOGet> setArticuloConsulta(@RequestBody ArticuloDTOGet articulo) throws NoSuchElementException, Exception{
        ArticuloDTOGet articuloConsultado = articuloService.setConsultar(articulo.id(), articulo.consultar());
        return new ResponseEntity<>(articuloConsultado, HttpStatus.OK);
    }

    @PutMapping("/isDestacado/{id}")
    public ResponseEntity<ArticuloDTOGet> setDestacado(@PathVariable Long id,@RequestBody IsDestacado destacado){
        ArticuloDTOGet articuloBd = articuloService.setDestacado(id, destacado);
        return new ResponseEntity<>(articuloBd, HttpStatus.OK);
    }
}
