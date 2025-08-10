package easycommerce.easycommerce.DetallePedido.Model;

import java.math.BigDecimal;

import easycommerce.easycommerce.Articulo.Model.Articulo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "articuloId")
    private Articulo articulo;
    private Integer cantidad;
    private BigDecimal precio_unitario;

    public BigDecimal calcularSubTotal(){
        return precio_unitario.multiply(BigDecimal.valueOf(cantidad));
    }

}
