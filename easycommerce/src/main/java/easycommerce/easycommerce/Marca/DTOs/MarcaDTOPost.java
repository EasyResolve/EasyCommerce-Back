package easycommerce.easycommerce.Marca.DTOs;

import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Marca.Model.Marca;

public record MarcaDTOPost(
    Marca marca,
    MultipartFile file
) {

}
