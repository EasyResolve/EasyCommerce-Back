package easycommerce.easycommerce.Pedido.DTOs;

import java.util.List;

public record PedidosDTOGestionPlus(
    String date_add,
    String id_order,
    String reference,
    List<CustomerGestionPlusDTO> customer,
    String shipping,
    String shipping_cost,
    Boolean message,
    String method_payment,
    List<ItemGestionPlusDTO> items,
    List<CustomFieldsGestionPlusDTO> custom_fields
) {

}
