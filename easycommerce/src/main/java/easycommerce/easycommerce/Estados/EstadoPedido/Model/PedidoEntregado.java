package backend_gnr.backend_gnr.Estados.EstadoPedido.model;

import java.util.List;

import backend_gnr.backend_gnr.Estados.CambioEstado.model.CambioEstado;
import backend_gnr.backend_gnr.Excepciones.InvalidStateChangeException;
import backend_gnr.backend_gnr.Pedido.model.Pedido;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PedidoEntregado")
public class PedidoEntregado extends EstadoPedido {

    @Override
    public String getEstado() throws Exception {
        return "Pedido Entregado";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede cancelar un pedido entregado");
    }

    @Override
    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede crear un pedido ya entregado");
    }

    @Override
    public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception {
        throw new InvalidStateChangeException("No se puede despachar un pedido que ya fue entregado");
    }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede preparar un pedido que ya se encuentra entregado");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra entregado correctamente");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra entregado");
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede pagar un pedido que ya fue entregado");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra pagado y entregado");
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede cancelar un pedido que ya fue entregado");
    }
    
}
