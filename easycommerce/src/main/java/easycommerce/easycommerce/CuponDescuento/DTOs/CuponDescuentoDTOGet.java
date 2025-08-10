package easycommerce.easycommerce.CuponDescuento.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CuponDescuentoDTOGet(
    Long id,
    String codigo,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    LocalDate fechaDesde,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    LocalDate fechaHasta,
    Integer cantidad,
    Double porcentajeDescuento,
    BigDecimal montoMaximoDeDescuento
) {

}
