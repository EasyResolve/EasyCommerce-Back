package easycommerce.easycommerce.Articulo.Model;

import java.util.List;

public record ArticuloConImagenesYErrores(
    List<ArticuloConImagenes> imagenes,
    List<String> errores
) {

}
