package easycommerce.easycommerce.Datos.Model;

import org.springframework.web.multipart.MultipartFile;

public record ImagenCarruselDTOPost(
    ImagenCarrusel imagenCarrusel,
    MultipartFile file
) {

}
