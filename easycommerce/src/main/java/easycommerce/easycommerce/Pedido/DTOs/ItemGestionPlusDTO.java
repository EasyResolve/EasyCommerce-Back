package easycommerce.easycommerce.Pedido.DTOs;

import java.util.List;

public record ItemGestionPlusDTO(
    String id_product,
    String reference,
    String id_product_attribute,
    String name,
    String quantity,                 
    String unit_price_tax_excl,
    String unit_price_tax_incl,
    String id_tax_rules_group,
    List<DiscountGestionPlusDTO> discount
) {

}
