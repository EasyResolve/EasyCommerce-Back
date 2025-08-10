package easycommerce.easycommerce.Datos.Model;

import org.springframework.web.multipart.MultipartFile;

public record TarjetaInformacionDTOPost(
    TarjetaInformacion informacion,
    MultipartFile file
) {

}
