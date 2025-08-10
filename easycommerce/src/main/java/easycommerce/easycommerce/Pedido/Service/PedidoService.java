package easycommerce.easycommerce.Pedido.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import backend_gnr.backend_gnr.Excepciones.NoSuchElementException;
import backend_gnr.backend_gnr.Excepciones.OutOfStockException;
import backend_gnr.backend_gnr.Excepciones.QuotationNotFoundException;
import backend_gnr.backend_gnr.Pedido.model.EstadosGetDTO;
import backend_gnr.backend_gnr.Pedido.model.Pedido;
import backend_gnr.backend_gnr.Pedido.model.PedidoConPago;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOGet;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOGetADMIN;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOPost;
import jakarta.mail.MessagingException;

public interface PedidoService {
    List<PedidoDTOGetADMIN> findAll() throws Exception;
    PedidoDTOGet findByIdMostrar(Long id) throws NoSuchElementException, Exception;
    PedidoConPago save(PedidoDTOPost pedido, String username) throws OutOfStockException, QuotationNotFoundException, IOException, NoSuchElementException, Exception;
    void delete(Long id) throws NoSuchElementException;
    List<PedidoDTOGet> findByCliente(Long clienteId) throws NoSuchElementException, Exception;
    Optional<Pedido> obtenerEstados(EstadosGetDTO datos) throws NoSuchElementException;
    PedidoDTOGet pedidoCreado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoPendienteDePago(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoPagado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoEnPreparacion(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoListoParaEntregar(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoDespachado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoEntregado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoCancelado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoRechazado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet saveCambioEstado(Pedido pedido) throws Exception;
    List<Pedido> findByFiltro(LocalDate fechaInicio, LocalDate fechaFin, String estado, String documento);
    Optional<Pedido> findById(Long id);
}
