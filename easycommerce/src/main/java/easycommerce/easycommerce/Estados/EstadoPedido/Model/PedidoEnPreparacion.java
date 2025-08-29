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
@DiscriminatorValue("PedidoEnPreparacion")
public class PedidoEnPreparacion extends EstadoPedido {

    @Override
    public void obtenerEstadosPosibles(TipoPago tipoPago, TipoEnvio tipoEnvio) throws Exception {
        List<String> estadosPosibles = new ArrayList<>();
        if(tipoEnvio == TipoEnvio.ENVIOADOMICILIO){
            estadosPosibles.add("EnCamino");
        } else {
            estadosPosibles.add("ListoParaEntregar");
        }
        estadosPosibles.add("Cancelar");
        setEstadosPosibles(estadosPosibles);
    }

    @Override
    public String getEstado() throws Exception {
        return "Pedido En Preparacion";
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
        throw new InvalidStateChangeException("El pedido se ecnuentra actualmente en preparacion");
    }

    @Override
    public Pedido pedidoEntregado(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("No se puede entregar un pedido que todavia se encuentra en preparacion");
    }

    @Override
    public Pedido pedidoPendienteDePago(Pedido pedido, List<CambioEstado> ce) throws Exception {
        throw new InvalidStateChangeException("El pedido ya se encuentra pagado");
    }

    @Override
    public Pedido pedidoListoParaEntregar(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if (pedido.getTipoEnvio() == TipoEnvio.RETIROENLOCAL) {
            finalizarCE(ce);
            PedidoListoParaEntregar listoParaEntregar = new PedidoListoParaEntregar();
            listoParaEntregar.setDescripcion("Pedido Listo Para Entregar");
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            cambioEstado.setDescripcion("El pedido N°: " + pedido.getId() + " "
                    + "ya se encuentra disponible para ser retirado en nuestro local" + "\n" + "Direccion: "
                    + "Intendente Riberi 502, Jovita, CBA");
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(listoParaEntregar);
            pedido.setEstadoActual(listoParaEntregar);
            return pedido;
        } else {
            throw new InvalidStateChangeException("El cliente seleccionó envío a domicilio...");
        }

    }

    @Override
    public Pedido pedidoEnCamino(Pedido pedido, List<CambioEstado> ce) throws Exception {
        if (pedido.getTipoEnvio() == TipoEnvio.ENVIOADOMICILIO) {
            finalizarCE(ce);
            PedidoEnCamino enCamino = new PedidoEnCamino();
            enCamino.setDescripcion("Pedido En Camino");
            CambioEstado cambioEstado = new CambioEstado();
            cambioEstado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
            cambioEstado
                    .setDescripcion("El pedido N°: " + pedido.getId() + " " + "se encuentra en camino a su domicilio");
            List<CambioEstado> cambiosEstados = pedido.getCambiosEstado();
            cambiosEstados.add(cambioEstado);
            pedido.setCambiosEstado(cambiosEstados);
            cambioEstado.setEstado(enCamino);
            pedido.setEstadoActual(enCamino);
            return pedido;
        } else {
            throw new InvalidStateChangeException("El cliente seleccionó retiro en local...");
        }
    }
}
