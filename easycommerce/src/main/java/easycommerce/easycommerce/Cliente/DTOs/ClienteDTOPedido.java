package easycommerce.easycommerce.Cliente.DTOs;

import easycommerce.easycommerce.Cliente.Model.CondicionIVA;
import easycommerce.easycommerce.Cliente.Model.TipoCliente;

public record ClienteDTOPedido(Long id,
String email,
String documento,
String nombre,
String apellido,
String razonSocial,
String telefono,
CondicionIVA condicionIva,
TipoCliente tipoCliente,
String calle,
String numero,
String ciudad,
String codigoPostal,
String provincia,
String pais) {
}
