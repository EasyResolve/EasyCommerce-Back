package easycommerce.easycommerce.Envio.Model;

import java.util.List;

public record ArmarEnvio(
    Double pesoTotal,
    Double volumenTotal,
    List<ArticuloItem> articulosAEmpaquetar
) {

}
