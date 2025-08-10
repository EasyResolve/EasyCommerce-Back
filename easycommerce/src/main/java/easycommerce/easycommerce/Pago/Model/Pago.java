package easycommerce.easycommerce.Pago.Model;

import java.math.BigDecimal;

import easycommerce.easycommerce.Datos.Model.DatosTransferencia;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;
    private BigDecimal total;
    private String codigoTransaccion;
    private String linkDePago;
    @ManyToOne
    @JoinColumn(name = "datosTransferencia")
    private DatosTransferencia datosTransferencia;
}
