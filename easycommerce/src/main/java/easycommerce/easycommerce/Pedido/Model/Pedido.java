package easycommerce.easycommerce.Pedido.Model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.DetallePedido.Model.DetallePedido;
import easycommerce.easycommerce.Direccion.Model.Direccion;
import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.EstadoPedido.Model.EstadoPedido;
import easycommerce.easycommerce.Pago.Model.TipoPago;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clienteId")
    private Cliente cliente;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private ZonedDateTime fechaCreacion;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "pedidoId")
    private List<DetallePedido> detalles;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pagoId")
    private Pago pago;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedidoId")
    private List<CambioEstado> cambiosEstado;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadoId")
    private EstadoPedido estadoActual;
    @Enumerated(EnumType.STRING)
    private TipoPago tipoEnvio;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccionId")
    private Direccion direccion;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedidoId")
    private List<Envio> envios;
    private String precioDolar;
    private String listaCliente;
    
    public BigDecimal calcularTotal(){
        BigDecimal total = new BigDecimal(0.0);
        for (DetallePedido detallePedido : detalles) {
            total = total.add(detallePedido.calcularSubTotal());
        }
        return total;
    }
}
