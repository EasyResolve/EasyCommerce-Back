package easycommerce.easycommerce.Pedido.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.OutOfStockException;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;
import easycommerce.easycommerce.Pedido.DTOs.EstadosGetDTO;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGet;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGetADMIN;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOPost;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.PedidoConPago;
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
    //PedidoDTOGet pedidoDespachado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoEntregado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoCancelado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet pedidoRechazado(Pedido pedido) throws MessagingException, NoSuchElementException, Exception;
    PedidoDTOGet saveCambioEstado(Pedido pedido) throws Exception;
    List<Pedido> findByFiltro(LocalDate fechaInicio, LocalDate fechaFin, String estado, String documento);
    Optional<Pedido> findById(Long id);
}
