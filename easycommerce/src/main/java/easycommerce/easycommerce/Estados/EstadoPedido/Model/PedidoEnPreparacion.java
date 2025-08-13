package easycommerce.easycommerce.Estados.EstadoPedido.Model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import easycommerce.easycommerce.Envio.Model.Envio;
import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Excepciones.InvalidStateChangeException;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Pago.Model.TipoPago;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.TipoEnvio;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PedidoEnPreparacion")
public class PedidoEnPreparacion extends EstadoPedido {

    @Override
    public String getEstado() throws Exception {
        return "Pedido En Preparacion";
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
        throw new InvalidStateChangeException("El pedido ya se encuentra creado correctamente");
    }

    // @Override
    // public Pedido pedidoDespachado(Pedido pedido, List<CambioEstado> ce, String codigosSeguimiento) throws Exception {
    //     if(pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO){
    //         finalizarCE(ce);
    //         PedidoDespachado despachado = new PedidoDespachado();
    //         despachado.setDescripcion("Pedido Despachado");
    //         List<String> estadosPosibles = new ArrayList<>();
    //         estadosPosibles.add("entregado");
    //         despachado.setEstadosPosibles(estadosPosibles);
    //         CambioEstado cambioEstado = new CambioEstado();
    //         cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
    //         String descripcion = "";
            
    //         if(!codigosSeguimiento.isEmpty()){
    //             String codigoSeguimiento = codigosSeguimiento;
    //             for (Envio envio : pedido.getEnvios()) {
    //                 envio.setCodigoSeguimiento(codigoSeguimiento);
    //             }
    //             descripcion = descripcion + "\n" + "Codigo de seguimiento de envio N° " + pedido.getEnvios().getFirst().getId() + ": " + pedido.getEnvios().getFirst().getCodigoSeguimiento();
    //             }
    //         else{
    //             throw new NoSuchElementException("Debe ingresar los codigos de seguimiento para cada uno de los envios pertenecientes al pedido");
    //         }
    //         cambioEstado.setDescripcion(descripcion);
    //         List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
    //         cambiosEstados.add(cambioEstado);
    //         pedido.setCambiosEstado(cambiosEstados);
    //         cambioEstado.setEstado(despachado);
    //         pedido.setEstadoActual(despachado);
    //         return pedido; 
    //     }
    //     else{
    //         throw new InvalidStateChangeException("No se puede despachar un pedido que va a ser retirado en el local por el cliente");
    //     }
             
    // }

    @Override
    public Pedido pedidoEnPreparacion(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido se ecnuentra actualmente en preparacion");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que todavia se encuentra en preparacion");
    }

    @Override
    public Pedido pedidoPagado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra pagado");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra pagado");
    }

    @Override
    public Pedido pedidoRechazado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        finalizarCE(ce);
        PedidoRechazado rechazado = new PedidoRechazado();
        rechazado.setDescripcion("Pedido Rechazado");
        List<String> estadoPosibles = new ArrayList<>();
        rechazado.setEstadosPosibles(estadoPosibles);
        CambioEstado cambioEstado = new CambioEstado();
        cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstado.setDescripcion("El pedido N°: " + pedido.getId() + " " + "fue rechazado");
        List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
        cambiosEstados.add(cambioEstado);
        pedido.setCambiosEstado(cambiosEstados);
        cambioEstado.setEstado(rechazado);
        pedido.setEstadoActual(rechazado);
        return pedido; 
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if(pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL){
            finalizarCE(ce);
            PedidoListoParaEntregar listoParaEntregar = new PedidoListoParaEntregar();
            listoParaEntregar.setDescripcion("Pedido Listo Para Entregar");
            List<String> estadoPosibles = new ArrayList<>();
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            estadoPosibles.add("cancelado");
            if(pedido.getPago().getTipoPago() == TipoPago.NAVE || pedido.getPago().getTipoPago() == TipoPago.TRANSFERENCIA){
                estadoPosibles.add("entregado");
                cambioEstado.setDescripcion("El pedido N°: " + pedido.getId() + " " + "ya se encuentra disponible para ser retirado en nuestro local" + "\n" + "Direccion: " + "Intendente Riberi 502, Jovita, CBA");
            }
            else{
                estadoPosibles.add("pendienteDePago");
                cambioEstado.setDescripcion("El pedido N°: " + pedido.getId() + " " + "ya se encuentra despachado a traves de correo argentino");
            }
            listoParaEntregar.setEstadosPosibles(estadoPosibles);
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(listoParaEntregar);
            pedido.setEstadoActual(listoParaEntregar);
            return pedido; 
        }
        else{
            throw new InvalidStateChangeException("No puede estar listo para entregar un pedido que debe ser despachado al domicilio del cliente");
        }
             
    }
}
