package easycommerce.easycommerce.Pedido.DTOs;

import java.time.LocalDate;

public record PedidoDTOFiltro(
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String estado,
    String documento
) {

}
