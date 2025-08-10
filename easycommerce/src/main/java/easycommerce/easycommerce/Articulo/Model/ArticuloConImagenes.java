package easycommerce.easycommerce.Articulo.Model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloConImagenes {
    private Long articuloId;
    private List<MultipartFile> Imagenes;
}
