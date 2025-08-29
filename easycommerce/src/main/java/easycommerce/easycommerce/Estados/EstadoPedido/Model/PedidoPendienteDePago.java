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
    public void obtenerEstadosPosibles(TipoPago tipoPago, TipoEnvio tipoEnvio) throws Exception{
        List<String> estadosPosibles = new ArrayList<>();
        if(tipoPago == TipoPago.EFECTIVO){
            estadosPosibles.add("AceptarPedido");
        } else{
            estadosPosibles.add("AceptarPago");
        }
        estadosPosibles.add("Cancelar");
        setEstadosPosibles(estadosPosibles);
    }

    @Override
    public String getEstado() throws Exception {
        return "Pedido Pendiente De Pago";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoCancelado cancelado = new PedidoCancelado();
        cancelado.setDescripcion("Pedido Cancelado");
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
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoEnPreparacion enPreparacion = new PedidoEnPreparacion();
        enPreparacion.setDescripcion("Pedido En Preparacion");
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido esta siendo preparado");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(enPreparacion);
        pedido.setEstadoActual(enPreparacion);
        return pedido;
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que todavia no fue preparado");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido se encuentra actualmente pendiente de pago");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede finalizar la preparacion de un pedido que todavia no esta siendo preparado");
    }

    @Override 
    public Pedido pedidoEnCamino(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede enviar un pedido que todavia no fue preparado"); 
    }

}
