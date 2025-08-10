package easycommerce.easycommerce.Envio.Model;

import easycommerce.easycommerce.Articulo.Model.Articulo;

public record ArticuloItem(
    Articulo articulo,
    Double volumen
) {

}
