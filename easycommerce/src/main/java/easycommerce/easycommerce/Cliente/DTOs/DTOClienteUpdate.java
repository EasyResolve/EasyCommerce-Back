package easycommerce.easycommerce.Cliente.DTOs;

import easycommerce.easycommerce.Cliente.Model.CondicionIVA;

public record DTOClienteUpdate(
    Long idCliente,
    CondicionIVA condicionIVA,
    String documento
) {

}
