package easycommerce.easycommerce.Estados.EstadoPedido.Model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Excepciones.InvalidStateChangeException;
import easycommerce.easycommerce.Pago.Model.TipoPago;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.TipoEnvio;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PedidoListoParaEntregar")
public class PedidoListoParaEntregar extends EstadoPedido {

    @Override
    public void obtenerEstadosPosibles(TipoPago tipoPago, TipoEnvio tipoEnvio) throws Exception{
        List<String> estadosPosibles = new ArrayList<>();
        estadosPosibles.add("Entregar");
        setEstadosPosibles(estadosPosibles);
    }

    @Override
    public String getEstado() throws Exception {
        return "Pedido Listo Para Entregar";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        // finalizarCE(ce);
        // PedidoCancelado cancelado = new PedidoCancelado();
        // cancelado.setDescripcion("Pedido Cancelado");
        // CambioEstado cambioEstado = new CambioEstado();
        // cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        // cambioEstado.setDescripcion("El pedido fue cancelado por el cliente");
        // List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        // cambiosEstados.add(cambioEstado);
        // pedido.setCambiosEstado(cambiosEstados);
        // cambioEstado.setEstado(cancelado);
        // pedido.setEstadoActual(cancelado);
        // return pedido;
        throw new InvalidStateChangeException("No se puede cancelar un pedido que ya está listo para entregar");
    }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra preparado y listo para retirar");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && (pedido.getPago().getTipoPago() == TipoPago.NAVE || pedido.getPago().getTipoPago() == TipoPago.TRANSFERENCIA)){
            finalizarCE(ce);
            PedidoEntregado entregado = new PedidoEntregado();
            entregado.setDescripcion("Pedido Entregado");
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            cambioEstado.setDescripcion("El pedido fue entregado al cliente en el local comercial");
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(entregado);
            pedido.setEstadoActual(entregado);
            return pedido;
        }
        else{
            throw new InvalidStateChangeException("No se puede entregar un pedido cuyo pago no fue efectuado");
        }
        
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra listo para entregar");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se ha aceptado");
    }

    @Override
    public Pedido pedidoEnCamino(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido es para retiro en el local");
    }

}
