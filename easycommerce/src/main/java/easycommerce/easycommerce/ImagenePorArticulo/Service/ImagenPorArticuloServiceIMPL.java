package easycommerce.easycommerce.ImagenePorArticulo.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.ImagenePorArticulo.Models.ImagenesPorArticulo;
import easycommerce.easycommerce.ImagenePorArticulo.Repository.ImagenesPorArticuloRepository;

@Service
public class ImagenPorArticuloServiceIMPL implements ImagenesPorArticuloService{

    private ImagenesPorArticuloRepository imagenesPorArticuloRepository;

    public ImagenPorArticuloServiceIMPL(ImagenesPorArticuloRepository imagenesPorArticuloRepository) {
        this.imagenesPorArticuloRepository = imagenesPorArticuloRepository;
    }

    @Override
    public Optional<ImagenesPorArticulo> findByNombreImagen(String nombre) {
        return imagenesPorArticuloRepository.findByDescripcion(nombre);
    }

    @Override
    public void saveImagenPorArticulo(ImagenesPorArticulo imagen) {
        imagenesPorArticuloRepository.save(imagen);
    }

    
}
