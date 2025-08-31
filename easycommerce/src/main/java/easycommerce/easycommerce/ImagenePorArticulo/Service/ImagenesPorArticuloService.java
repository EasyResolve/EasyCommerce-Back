package easycommerce.easycommerce.ImagenePorArticulo.Service;

import java.util.Optional;

import easycommerce.easycommerce.ImagenePorArticulo.Models.ImagenesPorArticulo;

public interface ImagenesPorArticuloService {
    Optional<ImagenesPorArticulo> findByNombreImagen(String nombre);
    void saveImagenPorArticulo(ImagenesPorArticulo imagen);
}   
