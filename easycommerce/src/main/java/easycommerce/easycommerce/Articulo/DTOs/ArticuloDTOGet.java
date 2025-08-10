package easycommerce.easycommerce.Articulo.DTOs;

import java.math.BigDecimal;
import java.util.List;

import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Rubro.Model.Rubro;
import easycommerce.easycommerce.SubRubro.Model.SubRubro;


public record ArticuloDTOGet(
    Long id,
    String nombre,
    String descripcion,
    Rubro rubro,
    SubRubro subRubro,
    Integer stockActual,
    BigDecimal precioOferta,
    BigDecimal preVtaPub1,
    boolean esOferta,
    boolean precioDolar,
    String fechaDesdeOferta,
    String fechaHastaOferta,
    boolean esNuevo,
    boolean activo,
    Double porcentajeOferta,
    String codigoOrigen,
    String codigoDeBarra,
    List<String> urlImagenes,
    Double alto,
    Double ancho,
    Double largo,
    Double peso,
    Double alicuotaIva,
    Marca marca,
    boolean consultar
) {

}
