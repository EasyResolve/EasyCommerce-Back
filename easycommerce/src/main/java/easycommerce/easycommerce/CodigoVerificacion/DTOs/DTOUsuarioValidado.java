package easycommerce.easycommerce.CodigoVerificacion.DTOs;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Rol.Model.Rol;

public record DTOUsuarioValidado(
    Long id,
    String username,
    Rol rol,
    Cliente cliente,
    boolean validado
) {

}
