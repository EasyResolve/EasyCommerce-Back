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

import easycommerce.easycommerce.Datos.Model.ImagenCarrusel;
import easycommerce.easycommerce.Datos.Model.ImagenCarruselDTOPost;
import easycommerce.easycommerce.Datos.Service.ImagenCarruselService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;


@RestController
@CrossOrigin
@RequestMapping("/imagenCarrusel")
public class ImagenCarruselController {

    private final ImagenCarruselService imagenCarruselService;

    public ImagenCarruselController(ImagenCarruselService imagenCarruselService) {
        this.imagenCarruselService = imagenCarruselService;
    }

    @GetMapping
    public ResponseEntity<List<ImagenCarrusel>> getAllImagenesCarrusel(){
        List<ImagenCarrusel> imagenes = imagenCarruselService.findAll();
        return new ResponseEntity<>(imagenes,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenCarrusel> getImagen(@PathVariable Long id){
        Optional<ImagenCarrusel> imagen = imagenCarruselService.findById(id);
        if(imagen.isPresent()){
            return new ResponseEntity<>(imagen.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ImagenCarrusel> saveImagenCarrusel(@RequestParam String nombre, @RequestParam String redireccionar, @RequestParam MultipartFile file, @RequestParam int orden) throws NoSuchElementException, IOException{
        ImagenCarrusel imagen = ImagenCarrusel.builder().nombre(nombre).redireccionar(redireccionar).orden(orden).build();
        ImagenCarruselDTOPost datosImagen = new ImagenCarruselDTOPost(imagen, file);
        ImagenCarrusel imagenGuardada = imagenCarruselService.save(datosImagen);
        return new ResponseEntity<>(imagenGuardada,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImagenCarrusel> updateImagenCarrusel(@PathVariable Long id, @RequestParam String nombre, @RequestParam String redireccionar, @RequestParam(required = false) String url , @RequestParam(required = false) MultipartFile file, @RequestParam(required = false) int orden) throws NoSuchElementException, IOException{
        ImagenCarrusel imagenCarrusel = ImagenCarrusel.builder().id(id).nombre(nombre).redireccionar(redireccionar).url(url).orden(orden).build();
        ImagenCarruselDTOPost datosImagen = new ImagenCarruselDTOPost(imagenCarrusel, file);
        ImagenCarrusel imagenGuardada = imagenCarruselService.save(datosImagen);
        return new ResponseEntity<>(imagenGuardada, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagenCarrusel(@PathVariable Long id) throws IOException{
        Optional<ImagenCarrusel> imagen = imagenCarruselService.findById(id);
        if(imagen.isPresent()){
            imagenCarruselService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
