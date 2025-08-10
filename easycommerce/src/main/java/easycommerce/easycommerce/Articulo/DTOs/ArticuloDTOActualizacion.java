package easycommerce.easycommerce.Articulo.DTOs;

import java.util.List;

public record ArticuloDTOActualizacion(
    List<Long> articulos,
    String codigoDescuento
) {

}