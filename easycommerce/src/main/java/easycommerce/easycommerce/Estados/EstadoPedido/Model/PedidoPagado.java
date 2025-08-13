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
@DiscriminatorValue("PedidoPagado")
public class PedidoPagado extends EstadoPedido {

    @Override
    public String getEstado() throws Exception {
        return "Pedido Confirmado";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoCancelado cancelado = new PedidoCancelado();
        cancelado.setDescripcion("Pedido Cancelado");
        List<String> estadosPosibles = new ArrayList<>();
        cancelado.setEstadosPosibles(estadosPosibles);
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido fue cancelado por el cliente" + "\n" + "Lamentamos que hayas cancelado el pedido" + "\n" + "Nos vemos pronto");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(cancelado);
        pedido.setEstadoActual(cancelado);
        return pedido;       
    }

    @Override
    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra creado correctamente");
    }

    // @Override
    // public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception {
    //     throw new InvalidStateChangeException("No se puede despachar un pedido que todavia no se preparo");
    // }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO || (pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && (pedido.getPago().getTipoPago() == TipoPago.NAVE || pedido.getPago().getTipoPago() == TipoPago.TRANSFERENCIA))){
            finalizarCE(ce);
            PedidoEnPreparacion enPreparacion = new PedidoEnPreparacion();
            enPreparacion.setDescripcion("Pedido En Preparacion");
            List<String> estadosPosibles = new ArrayList<>();
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            // if(pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO){
            //     estadosPosibles.add("despachado");
            //     cambioEstado.setDescripcion("El pedido esta siendo preparado para despacharlo por nuestro operadores");
            // }
            estadosPosibles.add("listoParaEntregar");
            cambioEstado.setDescripcion("El pedido esta siendo preparado por nuestro operadores");
            estadosPosibles.add("rechazado");
            estadosPosibles.add("cancelado");
            enPreparacion.setEstadosPosibles(estadosPosibles);
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(enPreparacion);
            pedido.setEstadoActual(enPreparacion);
            return pedido;    
        }
        else{
            throw new InvalidStateChangeException("El pedido ya fue preparado para retirar");
        }
        
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && pedido.getPago().getTipoPago() == TipoPago.EFECTIVO){
            finalizarCE(ce);
            PedidoEntregado entregado = new PedidoEntregado();
            entregado.setDescripcion("Pedido Entregado");
            List<String> estadosPosibles = new ArrayList<>();
            entregado.setEstadosPosibles(estadosPosibles);
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            cambioEstado.setDescripcion("El pedido fue entregado al cliente en el local");
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(entregado);
            pedido.setEstadoActual(entregado);
            return pedido;
        }
        else{
            throw new InvalidStateChangeException("El pedido no puede entregarse porque aun no fue preparado");
        }
        
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido actualmente esta pagado");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido actualmente esta pagado");
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra pagado por lo que no es posible rechazarlo");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("no se puede finalizar la preparacion de un pedido que todavia no esta siendo preparado");
    }

}
