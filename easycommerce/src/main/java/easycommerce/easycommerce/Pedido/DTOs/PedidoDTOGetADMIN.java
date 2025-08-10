package easycommerce.easycommerce.Pedido.DTOs;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.DetallePedido.Model.DetallePedido;
import easycommerce.easycommerce.Envio.Model.Envio;
import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.EstadoPedido.Model.EstadoPedido;
import easycommerce.easycommerce.Pago.Model.Pago;
import easycommerce.easycommerce.Pedido.Model.TipoEnvio;

public record PedidoDTOGetADMIN(
    Long id,
    Cliente cliente,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    ZonedDateTime fechaCreacion,
    List<DetallePedido> detalles,
    BigDecimal total,
    String estado,
    Pago pago,
    TipoEnvio tipoEnvio,
    EstadoPedido estadoActual,
    List<Envio> envios,
    CambioEstado cambiosEstado,
    String precioDolar,
    String listaCliente){
}
