package easycommerce.easycommerce.Estados.EstadoPedido.Model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


import easycommerce.easycommerce.Excepciones.InvalidStateChangeException;
import easycommerce.easycommerce.Pago.Model.TipoPago;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.TipoEnvio;
import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PedidoCreado")
public class PedidoCreado extends EstadoPedido{

    @Override
    public String getEstado() throws Exception {
        return "Pedido Creado";
    }

    @Override
    public Pedido pedidoCancelado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede cancelar un pedido recien creado");
    }

    @Override
    public Pedido pedidoCreado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido se encuentra actualmente en estado creado");
    }

    // @Override
    // public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigoSeguimiento) throws Exception {
    //     throw new InvalidStateChangeException("No se puede despachar un pedido que todavia no fue preparado");
    // }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && pedido.getPago().getTipoPago() == TipoPago.EFECTIVO){
            finalizarCE(ce);
            PedidoEnPreparacion enPreparacion = new PedidoEnPreparacion();
            enPreparacion.setDescripcion("Pedido En Preparacion");
            List<String> estadosPosibles = new ArrayList<>();
            estadosPosibles.add("listoParaEntregar");
            estadosPosibles.add("rechazado");
            estadosPosibles.add("cancelado");
            enPreparacion.setEstadosPosibles(estadosPosibles);
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            cambioEstado.setDescripcion("El pedido esta siendo preparado por nuestros operadores");
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(enPreparacion);
            pedido.setEstadoActual(enPreparacion);
            return pedido;
        }
        else{
            throw new InvalidStateChangeException("No se puede preparar un pedido que todavia no fue pagado para envio a domicilio");
        }
             
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que todavia no fue preparado");
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede pagar un pedido que no se encuentra pendiente de pago");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO || (pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL && (pedido.getPago().getTipoPago() == TipoPago.NAVE || pedido.getPago().getTipoPago() == TipoPago.TRANSFERENCIA))){
            finalizarCE(ce);
            PedidoPendienteDePago pendienteDePago = new PedidoPendienteDePago();
            pendienteDePago.setDescripcion("Pedido Pendiente De pago");
            List<String> estadosPosibles = new ArrayList<>();
            estadosPosibles.add("pagado");
            estadosPosibles.add("rechazado");
            estadosPosibles.add("cancelado");
            pendienteDePago.setEstadosPosibles(estadosPosibles);
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(pendienteDePago);
            pedido.setEstadoActual(pendienteDePago);
            return pedido;      
        }
        else{
            throw new InvalidStateChangeException("Si se selecciono la forma de envio retiro en el local y medio de pago efectivo no puede pasar a pendiente de pago hasta que no este listo para retirar");
        }
        
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede rechazar un pedido recien creado");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede terminar la preparacion de un pedido que todavia no se comenzo a preparar");
    }

}
