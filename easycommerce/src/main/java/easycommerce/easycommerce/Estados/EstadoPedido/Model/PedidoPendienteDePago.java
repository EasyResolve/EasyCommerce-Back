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
@DiscriminatorValue("PedidoPendienteDePago")
public class PedidoPendienteDePago extends EstadoPedido {

    @Override
    public String getEstado() throws Exception {
        return "Pedido Pendiente De Pago";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoCancelado cancelado = new PedidoCancelado();
        cancelado.setDescripcion("Pedido Cancelado");
        List<String> estadoPosibles = new ArrayList<>();
        cancelado.setEstadosPosibles(estadoPosibles);
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido fue cancelado por el cliente");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(cancelado);
        pedido.setEstadoActual(cancelado);
        return pedido;
    }

    @Override
    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se ecnuentra creado correctamente");
    }

    // @Override
    // public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception {
    //     throw new InvalidStateChangeException("No se puede despachar un pedido que todavia no fue preparado");
    // }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede preparar un pedido que todavia no fue pagado");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que todavia no fue preparado");
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoPagado pagado = new PedidoPagado();
        pagado.setDescripcion("Pedido Pagado");
        List<String> estadosPosibles = new ArrayList<>();
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && pedido.getPago().getTipoPago() != TipoPago.NAVE){
            estadosPosibles.add("entregado");
        }
        // if (pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO || pedido.getPago().getTipoPago() == TipoPago.NAVE){
        //     estadosPosibles.add("enPreparacion");
        // }
        estadosPosibles.add("cancelado");
        pagado.setEstadosPosibles(estadosPosibles);
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido ya se encuentra pagado por el cliente a traves del medio especificado");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(pagado);
        pedido.setEstadoActual(pagado);
        return pedido;
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido se encuentra actualmente pendiente de pago");
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoRechazado rechazado = new PedidoRechazado();
        rechazado.setDescripcion("Pedido Rechazado");
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido fue rechazado por el local");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(rechazado);
        pedido.setEstadoActual(rechazado);
        return pedido;
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede finalizar la preparacion de un pedido que todavia no esta siendo preparado");
    }

    

}
