package easycommerce.easycommerce.Pedido.DTOs;

public record AddressGestionPlusDTO(
    String empresa,
    String cuit,
    String dni,
    String phone_mobile,
    String phone,
    String address1,
    String address2,
    String city,
    String state,
    String postcode
) {

}
