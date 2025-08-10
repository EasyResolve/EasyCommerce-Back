package easycommerce.easycommerce.Envio.Model;

import java.math.BigDecimal;
import java.util.List;

public record ArmarEnvioConCosto(
    Double pesoTotal,
    Double volumenTotal,
    List<ArticuloItem> articulosAEmpaquetar,
    BigDecimal costoBulto
) {

}
