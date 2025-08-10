package easycommerce.easycommerce.Articulo.DTOs;

import java.math.BigDecimal;

public record ArticuloDTOPost(Long id,
String nombre,
String descripcion,
Long rubros_id,
Long sub_rubros_id,
String tags,
String fotos,
Integer cantidad,
BigDecimal precio_oferta,
Integer precio_dolar,
BigDecimal precio_1,
BigDecimal precio_2,
BigDecimal precio_3,
BigDecimal precio_m_1,
BigDecimal precio_m_2,
BigDecimal precio_m_3,
Integer es_oferta,
String desde_fecha_oferta,
String hasta_fecha_oferta,
Integer es_nuevo,
Integer activo,
Double porc_oferta,
Integer pisar_fotos,
String CodOrigen,
String CodDeBarra,
Double Medidas_alto,
Double Medidas_ancho,
Double Medidas_largo,
Double peso,
Double aliiva,
Long marca,
Integer PrecioFinal) {

}
