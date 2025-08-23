package easycommerce.easycommerce.Rubro.DTOs;

import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Rubro.Model.Rubro;

public record RubroDTOPut(
    Rubro rubro,
    MultipartFile file
) {

}
