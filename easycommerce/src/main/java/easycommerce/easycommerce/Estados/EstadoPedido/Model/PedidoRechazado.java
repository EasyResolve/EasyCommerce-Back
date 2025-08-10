package easycommerce.easycommerce.Estados.EstadoPedido.Model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import backend_gnr.backend_gnr.Estados.CambioEstado.model.CambioEstado;
import backend_gnr.backend_gnr.Excepciones.InvalidStateChangeException;
import backend_gnr.backend_gnr.Pedido.model.Pedido;
import backend_gnr.backend_gnr.Pedido.model.TipoEnvio;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PedidoRechazado")
public class PedidoRechazado extends EstadoPedido {

    @Override
    public String getEstado() throws Exception {
        return "Pedido Rechazado";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("no se puede cancelar un pedido que fue rechazado");
    }

    @Override
    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido se encuentra cancelado");
    }

    @Override
    public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception {
        throw new InvalidStateChangeException("No se puede despachar un pedido rechazado");
    }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede preparar un pedido que fue rechazado");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que fue rechazado");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que fue rechazado");
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoPagado pagado = new PedidoPagado();
        pagado.setDescripcion("Pedido Pagado");
        List<String> estadosPosibles = new ArrayList<>();
        if(pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO){
            estadosPosibles.add("enPreparacion");
        }
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL){
            estadosPosibles.add("entregado");
        }
        pagado.setEstadosPosibles(estadosPosibles);
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido fue pagado exitosamente");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(pagado);
        pedido.setEstadoActual(pagado);
        return pedido;      
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede pagar un pedido que fue rechazado");
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra rechazado");
    }

}
