package easycommerce.easycommerce.Correo;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Pedido.Model.Pedido;

public record DatosCorreo(Pedido pedido,
CambioEstado cambioEstadoActual) {
}
