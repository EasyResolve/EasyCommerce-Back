package easycommerce.easycommerce.Usuario.DTOs;

public record DTOPostUsuario(
    String username,
    String documento,
    String nombre,
    String apellido,
    String razonSocial,
    String password,
    Long rolId
) {

}
