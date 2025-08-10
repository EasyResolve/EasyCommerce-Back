package easycommerce.easycommerce.Estados.Estado.Model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Excepciones.InvalidStateChangeException;
import backend_gnr.backend_gnr.Pago.model.Pago;
import backend_gnr.backend_gnr.Pedido.model.Pedido;
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

    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }

    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception{
        throw new Exception();
    }
    
    public String getEstado() throws Exception{
        throw new Exception();
    }

}
