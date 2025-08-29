package easycommerce.easycommerce.Pedido.DTOs;

public record CustomerGestionPlusDTO(
    Long id_customer,
    String lastname,
    String firstname,
    String email,
    AddressGestionPlusDTO address
) {

}
