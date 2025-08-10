package easycommerce.easycommerce.Envio.Model;

import java.math.BigDecimal;
import java.util.List;

public record EnvioConDatos(
    List<ArmarEnvioConCosto> bultos,
    BigDecimal costoEnvio
) {

}
