package easycommerce.easycommerce.Pedido.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.Estado.DTOs.EstadoDTOGet;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Pedido.DTOs.EstadosGetDTO;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOFiltro;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGet;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGetADMIN;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOPost;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.PedidoConPago;
import easycommerce.easycommerce.Pedido.Service.PedidoService;



@RestController
@CrossOrigin
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTOGetADMIN>> getAllPedidos() throws Exception{
        List<PedidoDTOGetADMIN> pedidos = pedidoService.findAll();
        return new ResponseEntity<>(pedidos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTOGet> getPedido(@PathVariable Long id) throws Exception{
        PedidoDTOGet pedido = pedidoService.findByIdMostrar(id);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoDTOGet>> getPedidosPorCliente(@PathVariable Long id) throws Exception{
        List<PedidoDTOGet> pedidos = pedidoService.findByCliente(id);
        return new ResponseEntity<>(pedidos,HttpStatus.OK);
    }

    @PostMapping("/filtros")
    public ResponseEntity<List<PedidoDTOGet>> getPedidosFiltro(@RequestBody PedidoDTOFiltro filtros) throws Exception{
        List<PedidoDTOGet> pedidosAMostrar = new ArrayList<>();
        List<Pedido> pedidosFiltrados = pedidoService.findByFiltro(filtros.fechaInicio(), filtros.fechaFin(), filtros.estado(), filtros.documento());
        for (Pedido pedido : pedidosFiltrados) {
            Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
            .filter(CambioEstado::esActual)
            .findFirst();
            PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(), pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(),pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
            pedidosAMostrar.add(pedidoAMostrar);
        }
        return new ResponseEntity<>(pedidosAMostrar,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PedidoConPago> savePedido(@RequestBody PedidoDTOPost pedido) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        PedidoConPago pedidoGuardado = pedidoService.save(pedido, username);
        return new ResponseEntity<>(pedidoGuardado,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) throws NoSuchElementException{
        pedidoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/ListoParaEntregar") // Para pasar a "Listo para Entregar"
    public ResponseEntity<PedidoDTOGet> listoParaEntregar(@PathVariable Long id) throws Exception{
        Optional<Pedido> pedidoBd = pedidoService.findById(id);
        if(pedidoBd.isPresent()){
            Pedido pedidoAGuardar = pedidoBd.get().getEstadoActual().pedidoListoParaEntregar(pedidoBd.get(), pedidoBd.get().getCambiosEstado());
            PedidoDTOGet pedidoGuardado = pedidoService.pedidoListoParaEntregar(pedidoAGuardar);
            return new ResponseEntity<>(pedidoGuardado,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    @PutMapping("/{id}/Entregar") // Para pasar a "Entregado"
    public ResponseEntity<PedidoDTOGet> entregado(@PathVariable Long id) throws Exception{
        Optional<Pedido> pedidoBd = pedidoService.findById(id);
        if(pedidoBd.isPresent()){
            Pedido pedidoAGuardar = pedidoBd.get().getEstadoActual().pedidoEntregado(pedidoBd.get(), pedidoBd.get().getCambiosEstado());
            PedidoDTOGet pedidoGuardado = pedidoService.pedidoEntregado(pedidoAGuardar);
            return new ResponseEntity<>(pedidoGuardado,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    @PutMapping("/{id}/Cancelar") // Para pasar a "Cancelado"
    public ResponseEntity<PedidoDTOGet> cancelado(@PathVariable Long id) throws Exception{
        Optional<Pedido> pedidoBd = pedidoService.findById(id);
        if(pedidoBd.isPresent()){
            Pedido pedidoAGuardar = pedidoBd.get().getEstadoActual().pedidoCancelado(pedidoBd.get(), pedidoBd.get().getCambiosEstado());
            PedidoDTOGet pedidoGuardado = pedidoService.pedidoCancelado(pedidoAGuardar);
            return new ResponseEntity<>(pedidoGuardado,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @PutMapping("/{id}/AceptarPago") // Para pasar a "En Preparacion"
    public ResponseEntity<PedidoDTOGet> enPreparacion(@PathVariable Long id) throws Exception{
        Optional<Pedido> pedidoBd = pedidoService.findById(id);
        if(pedidoBd.isPresent()){
            Pedido pedidoAGuardar = pedidoBd.get().getEstadoActual().pedidoEnPreparacion(pedidoBd.get(), pedidoBd.get().getCambiosEstado());
            PedidoDTOGet pedidoGuardado = pedidoService.pedidoEnPreparacion(pedidoAGuardar);
            return new ResponseEntity<>(pedidoGuardado,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    @PostMapping("/estados")
    public ResponseEntity<List<CambioEstado>> getEstados(@RequestBody EstadosGetDTO datos) throws Exception{
        List<EstadoDTOGet> estados = new ArrayList<>();
        Optional<Pedido> pedido = pedidoService.obtenerEstados(datos);
        if(pedido.isPresent()){
            List<CambioEstado> ce = pedido.get().getCambiosEstado();
            for (CambioEstado cambioEstado : ce) {
                EstadoDTOGet estado = new EstadoDTOGet(cambioEstado.getFechaInicio().toString(),cambioEstado.getEstado().getEstado(),cambioEstado.getDescripcion());
                estados.add(estado);
            }
            return new ResponseEntity<>(ce,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

}
