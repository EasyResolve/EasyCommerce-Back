package easycommerce.easycommerce.Datos.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Datos.Model.ImagenCarrusel;
import easycommerce.easycommerce.Datos.Model.ImagenCarruselDTOPost;
import easycommerce.easycommerce.Datos.Repository.ImagenCarruselRepository;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;

@Service
public class ImagenCarruselServiceIMPL implements ImagenCarruselService {

    private final ImagenCarruselRepository imagenCarruselRepository;

    public ImagenCarruselServiceIMPL(ImagenCarruselRepository imagenCarruselRepository) {
        this.imagenCarruselRepository = imagenCarruselRepository;
    }

    @Override
    public List<ImagenCarrusel> findAll() {
        return imagenCarruselRepository.findAllOrdenadas();
    }

    @Override
    public Optional<ImagenCarrusel> findById(Long id) {
        return imagenCarruselRepository.findById(id);
    }

    @Override
    public ImagenCarrusel save(ImagenCarruselDTOPost imagenCarrusel) throws NoSuchElementException, IOException {
        ImagenCarrusel imagenNueva = new ImagenCarrusel();
        if(imagenCarrusel.imagenCarrusel().getId() != null){
            Optional<ImagenCarrusel> imagenBd = imagenCarruselRepository.findById(imagenCarrusel.imagenCarrusel().getId());
            if(imagenBd.isPresent()){
                imagenNueva = imagenBd.get();
            }
            else{
                throw new NoSuchElementException("No existe una imagen carrusel con id: " + imagenCarrusel.imagenCarrusel().getId());
            }
        }
        if(imagenCarrusel.imagenCarrusel().getNombre() != null){
            imagenNueva.setNombre(imagenCarrusel.imagenCarrusel().getNombre());
        }

        if(imagenCarrusel.imagenCarrusel().getRedireccionar() != null){
            imagenNueva.setRedireccionar(imagenCarrusel.imagenCarrusel().getRedireccionar());
        }

        if(imagenCarrusel.imagenCarrusel().getOrden() != 0){
            imagenNueva.setOrden(imagenCarrusel.imagenCarrusel().getOrden());
        }

        imagenNueva = imagenCarruselRepository.save(imagenNueva);
        
        if(imagenCarrusel.file() != null){
            List<MultipartFile> files = new ArrayList<>();
            files.add(imagenCarrusel.file());
            List<String> rutas = ImageStorageUtil.saveImages(files, "imagenes/imagenCarrusel/imagenCarrusel_" + imagenNueva.getId());
            if(!rutas.isEmpty()){
                imagenNueva.setUrl(rutas.getFirst());
            }
            imagenNueva = imagenCarruselRepository.save(imagenNueva);
        }
        return imagenNueva;
    }

    @Override
    public void delete(Long id) throws IOException {
        Optional<ImagenCarrusel> imagenCarrusel = imagenCarruselRepository.findById(id);
        if(imagenCarrusel.isPresent()){
            List<String> rutaImagen = List.of(imagenCarrusel.get().getUrl());
            if(!rutaImagen.isEmpty()){
                ImageStorageUtil.deleteImage(rutaImagen);
            }
            imagenCarruselRepository.deleteById(id);
        }
    }

    
}
