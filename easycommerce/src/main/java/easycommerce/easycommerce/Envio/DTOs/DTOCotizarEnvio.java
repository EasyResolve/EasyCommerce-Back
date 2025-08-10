package easycommerce.easycommerce.Envio.DTOs;

import java.util.List;

import easycommerce.easycommerce.DetallePedido.DTOs.DetallePedidoDTOPost;

public record DTOCotizarEnvio(
    List<DetallePedidoDTOPost> detalles,
    String cuponDescuento,
    String codigoPostal,
    String ciudad
) {

}
