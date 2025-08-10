package easycommerce.easycommerce.Usuario.DTOs;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Rol.Model.Rol;

public record UsuarioDTOGet(
    Long id,
    String username,
    Rol rol,
    Cliente cliente,
    boolean validado
) {

}
