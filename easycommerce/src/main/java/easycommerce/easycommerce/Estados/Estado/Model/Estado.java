package easycommerce.easycommerce.Estados.Estado.Model;

// import java.beans.Transient; // Removed incorrect import
import jakarta.persistence.Transient;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Pago.Model.Pago;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @Transient
    private List<String> estadosPosibles;

    public void finalizarCE(List<CambioEstado> ce)
    {
        ZonedDateTime fecha = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")); 
        ce.stream()
        .filter(CambioEstado::esActual)
        .findFirst()
        .ifPresent(cambioestado -> cambioestado.setFechaFin(fecha));
    }

    public Pago pagoPendiente(Pago pago, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pago pagoEnProceso(Pago pago, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pago pagoConcretado(Pago pago, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pago pagoRechazado(Pago pago, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }

    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }

    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }

    public Pedido pedidoEnCamino(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }

    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }

    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new Exception();
    }
    
    public String getEstado() throws Exception{
        throw new Exception();
    }

    public void obtenerEstadosPosibles() throws Exception {
        throw new Exception();
    }

}
