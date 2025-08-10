package easycommerce.easycommerce.Pedido.DTOs;

import java.util.List;

import easycommerce.easycommerce.Cliente.DTOs.ClienteDTOPedido;
import easycommerce.easycommerce.DetallePedido.DTOs.DetallePedidoDTOPost;

public record PedidoDTOPost(ClienteDTOPedido cliente,
List<DetallePedidoDTOPost> detalles,
Integer formaPago,
Integer tipoEnvio,
boolean esCelular,
String cuponDescuento) {
}
