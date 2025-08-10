package easycommerce.easycommerce.Pedido.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import backend_gnr.backend_gnr.Articulo.model.ArticuloDTOGet;
import backend_gnr.backend_gnr.Articulo.service.ArticuloService;
import backend_gnr.backend_gnr.Excepciones.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

import backend_gnr.backend_gnr.Articulo.model.Articulo;
import backend_gnr.backend_gnr.Articulo.repository.ArticuloRepository;
import backend_gnr.backend_gnr.Caja.model.Caja;
import backend_gnr.backend_gnr.Caja.repository.CajaRepository;
import backend_gnr.backend_gnr.Cliente.model.Cliente;
import backend_gnr.backend_gnr.Cliente.model.TipoCliente;
import backend_gnr.backend_gnr.Cliente.repository.ClienteRepository;
import backend_gnr.backend_gnr.CodigoPostalZona.models.CodigoPostalZona;
import backend_gnr.backend_gnr.CodigoPostalZona.repository.CodigoPostalZonaRepository;
import backend_gnr.backend_gnr.Correo.CorreoService;
import backend_gnr.backend_gnr.Cotizacion.model.Cotizacion;
import backend_gnr.backend_gnr.Cotizacion.repository.CotizacionRepository;
import backend_gnr.backend_gnr.Cotizacion.service.CotizacionService;
import backend_gnr.backend_gnr.CuponDescuento.model.CuponDescuento;
import backend_gnr.backend_gnr.CuponDescuento.repository.CuponDescuentoRepository;
import backend_gnr.backend_gnr.Datos.models.DatosTransferencia;
import backend_gnr.backend_gnr.Datos.repository.DatosTransferenciaRepository;
import backend_gnr.backend_gnr.DetallePedido.model.DetallePedido;
import backend_gnr.backend_gnr.DetallePedido.model.DetallePedidoDTOPost;
import backend_gnr.backend_gnr.DetallePedido.repository.DetallePedidoRepository;
import backend_gnr.backend_gnr.Direccion.model.Direccion;
import backend_gnr.backend_gnr.Direccion.service.DireccionService;
import backend_gnr.backend_gnr.Envio.models.ArmarEnvio;
import backend_gnr.backend_gnr.Envio.models.ArmarEnvioConCosto;
import backend_gnr.backend_gnr.Envio.models.ArticuloItem;
import backend_gnr.backend_gnr.Envio.models.DTOCotizarEnvio;
import backend_gnr.backend_gnr.Envio.models.Envio;
import backend_gnr.backend_gnr.Envio.models.EnvioConDatos;
import backend_gnr.backend_gnr.Envio.service.CotizarEnvioService;
import backend_gnr.backend_gnr.Estados.CambioEstado.model.CambioEstado;
import backend_gnr.backend_gnr.Estados.CambioEstado.repository.CambioEstadoRepository;
import backend_gnr.backend_gnr.Estados.Estado.model.EstadoDTOGet;
import backend_gnr.backend_gnr.Estados.Estado.repository.EstadoRepository;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.EstadoPedido;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.PedidoCancelado;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.PedidoCreado;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.PedidoEnPreparacion;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.PedidoPendienteDePago;
import backend_gnr.backend_gnr.Estados.EstadoPedido.model.PedidoRechazado;
import backend_gnr.backend_gnr.Nave.models.ResponsePago;
import backend_gnr.backend_gnr.Nave.service.NaveService;
import backend_gnr.backend_gnr.Pago.model.Pago;
import backend_gnr.backend_gnr.Pago.model.TipoPago;
import backend_gnr.backend_gnr.Pago.service.PagoService;
import backend_gnr.backend_gnr.Parametros.model.ListaPrecioMayorista;
import backend_gnr.backend_gnr.Parametros.model.ListaPrecioMinorista;
import backend_gnr.backend_gnr.Parametros.model.Parametro;
import backend_gnr.backend_gnr.Parametros.repository.ParametroRepository;
import backend_gnr.backend_gnr.Pedido.model.EstadosGetDTO;
import backend_gnr.backend_gnr.Pedido.model.Pedido;
import backend_gnr.backend_gnr.Pedido.model.PedidoConPago;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOGet;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOGetADMIN;
import backend_gnr.backend_gnr.Pedido.model.PedidoDTOPost;
import backend_gnr.backend_gnr.Pedido.model.TipoEnvio;
import backend_gnr.backend_gnr.Pedido.repository.PedidoRepository;
import backend_gnr.backend_gnr.Usuario.model.Usuario;
import backend_gnr.backend_gnr.Usuario.repository.UsuarioRepository;
import io.micrometer.core.instrument.config.validate.Validated.Invalid;

@Service
public class PedidoServiceIMPL implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ArticuloRepository articuloRepository;
    private final ClienteRepository clienteRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionService cotizacionService;
    private final PagoService pagoService;
    private final CambioEstadoRepository cambioEstadoRepository;
    private final EstadoRepository estadoRepository;
    private final CorreoService correoService;
    private final DireccionService direccionService;
    private final NaveService naveService;
    private final DatosTransferenciaRepository datosTransferenciaRepository;
    private final CotizarEnvioService cotizarEnvioService;
    private final CajaRepository cajaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParametroRepository parametroRepository;
    private final CodigoPostalZonaRepository codigoPostalZonaRepository;
    private final CuponDescuentoRepository cuponDescuentoRepository;
    private final ArticuloService articuloService;
    public PedidoServiceIMPL(PedidoRepository pedidoRepository, ArticuloRepository articuloRepository,
            ClienteRepository clienteRepository, DetallePedidoRepository detallePedidoRepository,
            CotizacionRepository cotizacionRepository, CotizacionService cotizacionService, PagoService pagoService,
            CambioEstadoRepository cambioEstadoRepository, EstadoRepository estadoRepository,
            CorreoService correoService, DireccionService direccionService, NaveService naveService,
            DatosTransferenciaRepository datosTransferenciaRepository, CotizarEnvioService cotizarEnvioService,
            CajaRepository cajaRepository, UsuarioRepository usuarioRepository, ParametroRepository parametroRepository,
            CodigoPostalZonaRepository codigoPostalZonaRepository, CuponDescuentoRepository cuponDescuentoRepository,
            ArticuloService articuloService) {
                this.articuloService = articuloService;
        this.pedidoRepository = pedidoRepository;
        this.articuloRepository = articuloRepository;
        this.clienteRepository = clienteRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.cotizacionService = cotizacionService;
        this.pagoService = pagoService;
        this.cambioEstadoRepository = cambioEstadoRepository;
        this.estadoRepository = estadoRepository;
        this.correoService = correoService;
        this.direccionService = direccionService;
        this.naveService = naveService;
        this.datosTransferenciaRepository = datosTransferenciaRepository;
        this.cotizarEnvioService = cotizarEnvioService;
        this.cajaRepository = cajaRepository;
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository = parametroRepository;
        this.codigoPostalZonaRepository = codigoPostalZonaRepository;
        this.cuponDescuentoRepository = cuponDescuentoRepository;
    }

    @Override
    @Transactional
    public List<PedidoDTOGetADMIN> findAll() throws Exception {
        List<PedidoDTOGetADMIN> pedidosAMostrar = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
            .filter(CambioEstado::esActual)
            .findFirst();
            PedidoDTOGetADMIN pedidoDTO = new PedidoDTOGetADMIN(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(),pedido.getPago(), pedido.getTipoEnvio(),pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
            pedidosAMostrar.add(pedidoDTO);
        }
        return pedidosAMostrar;
    }

    @Override
    @Transactional
    public PedidoDTOGet findByIdMostrar(Long id) throws Exception {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if(pedido.isPresent()){
            Optional<CambioEstado> ultimoCE = pedido.get().getCambiosEstado().stream()
            .filter(CambioEstado::esActual)
            .findFirst();
            PedidoDTOGet pedidoDTO = new PedidoDTOGet(pedido.get().getId(), pedido.get().getCliente(), pedido.get().getFechaCreacion(), pedido.get().getDetalles(), pedido.get().calcularTotal(),pedido.get().getEstadoActual().getEstado(),pedido.get().getPago(), pedido.get().getTipoEnvio(), pedido.get().getEstadoActual(), pedido.get().getEnvios(), ultimoCE.get());
            return pedidoDTO;
        }
        else{
            throw new NoSuchElementException("No se encuentra registrado el pedido con id: " + pedido.get().getId());
        }
    }
    private BigDecimal calcularDescuento(BigDecimal precio, BigDecimal porcentaje, BigDecimal maximo) {
        if (porcentaje.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal descuentoCalculado = precio.multiply(porcentaje)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return descuentoCalculado.compareTo(maximo) > 0 ? maximo : descuentoCalculado;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PedidoConPago save(PedidoDTOPost pedido, String username) throws Exception {     
        //Tomo los detalles de pedido que envia el usuario
        List<DetallePedidoDTOPost> detalles = pedido.detalles();
        //Creo un una nueva lista con los detalles de pedido a guardar
        List<DetallePedido> detallesAGuardar = new ArrayList<>();
        //Creo un nuevo pedido
        Pedido pedidoAGuardar = new Pedido();
        //Creo el dto a devolver
        PedidoConPago pedidoConPago = new PedidoConPago();

        Double porcentajeDescuento = 0.0;
        BigDecimal maximoDescuento = BigDecimal.valueOf(0);

        Cliente clienteNuevo = null;
        //me llega el cliente y ya lo registro
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                clienteNuevo = usuario.get().getCliente();
                clienteNuevo.setNombre(pedido.cliente().nombre());
                clienteNuevo.setApellido(pedido.cliente().apellido());
                clienteNuevo.setCondicionIva(pedido.cliente().condicionIva());
                clienteNuevo.setDocumento(pedido.cliente().documento());
                clienteNuevo.setEmail(pedido.cliente().email());
                clienteNuevo.setTelefono(pedido.cliente().telefono());
                clienteNuevo.setRazonSocial(pedido.cliente().razonSocial());
                clienteNuevo = clienteRepository.save(clienteNuevo);

                pedidoAGuardar.setListaCliente("Cliente Logueado: " + clienteNuevo.getTipoCliente().name() + " " + clienteNuevo.getLista());
            }
            else{
                throw new NoSuchElementException("No se encontro un usuario con el username: " + username);
            }
        }
        else{
            ListaPrecioMinorista listaMinorista = this.parametroRepository.findByDescripcion("listaPrecioMinorista").get().getListaPrecioMinorista();
            pedidoAGuardar.setListaCliente("Cliente no Logueado: " + "MINORISTA" + " " + listaMinorista.getCodigo());
            //Creo un nuevo cliente asignando en los atributos del objeto los que me estan llegando dentro del pedido
            clienteNuevo = new Cliente();
            clienteNuevo.setNombre(pedido.cliente().nombre());
            clienteNuevo.setApellido(pedido.cliente().apellido());
            clienteNuevo.setCondicionIva(pedido.cliente().condicionIva());
            clienteNuevo.setDocumento(pedido.cliente().documento());
            clienteNuevo.setEmail(pedido.cliente().email());
            clienteNuevo.setTelefono(pedido.cliente().telefono());
            clienteNuevo.setRazonSocial(pedido.cliente().razonSocial());
            clienteNuevo.setTipoCliente(TipoCliente.MINORISTA);
            List<Direccion> direcciones = new ArrayList<>(); 
            clienteNuevo.setDirecciones(direcciones);
        }
        //Verifico que el cliente exista
        Optional<Cliente> cliente = clienteRepository.findByDocumento(clienteNuevo.getDocumento());
        if(cliente.isPresent()){
            cliente.get().setNombre(clienteNuevo.getNombre());
            cliente.get().setApellido(clienteNuevo.getApellido());
            cliente.get().setDocumento(clienteNuevo.getDocumento());
            cliente.get().setEmail(clienteNuevo.getEmail());
            cliente.get().setRazonSocial(clienteNuevo.getRazonSocial());
            cliente.get().setTelefono(clienteNuevo.getTelefono());
            cliente.get().setCondicionIva(clienteNuevo.getCondicionIva());
            cliente.get().setTipoCliente(clienteNuevo.getTipoCliente());
            pedidoAGuardar.setCliente(cliente.get());
        }
        else{
            Cliente clienteGuardado = clienteRepository.save(clienteNuevo);
            pedidoAGuardar.setCliente(clienteGuardado);
        }

        //verifico si existe la direccion guardada para ese cliente
        Pattern patron = Pattern.compile("\\d+");
        Matcher coincidencias = patron.matcher(pedido.cliente().codigoPostal());
        String codigoPostal = coincidencias.find() ? coincidencias.group() : "";
        Optional<Direccion> direccionExistente = direccionService.verify(pedido.cliente().calle(), pedido.cliente().numero(), pedido.cliente().ciudad(), codigoPostal, pedido.cliente().provincia(), "Argentina", pedidoAGuardar.getCliente().getId());
        //Si la direccion existe la seteo al pedido
        if(direccionExistente.isPresent()){
            pedidoAGuardar.setDireccion(direccionExistente.get());
        }
        //Si no existe la direccion se crea una nueva con los datos enviados por el usuario
        else{
            Direccion direccionNueva = new Direccion();
            direccionNueva.setCalle(pedido.cliente().calle());
            direccionNueva.setCiudad(pedido.cliente().ciudad());
            direccionNueva.setCodigoPostal(codigoPostal);
            direccionNueva.setPais("Argentina");
            direccionNueva.setProvincia(pedido.cliente().provincia());
            direccionNueva.setNumero(pedido.cliente().numero());
            direccionNueva = direccionService.save(direccionNueva);
            pedidoAGuardar.getCliente().getDirecciones().add(direccionNueva);
            pedidoAGuardar.setDireccion(direccionNueva);
        }  
        
         // 5. Obtener artículos y procesar precios
        List<Articulo> articulos = detalles.stream()
        .map(d -> articuloRepository.findById(d.idArticulo()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
        // 4. Obtener cotización USD
    Cotizacion cotizacion = cotizacionRepository.findByMoneda("USD")
        .orElseGet(() -> {
            try {
                return cotizacionService.obtenerCotizacion("USD");
            } catch (QuotationNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    Cliente clienteBd = null;
    if(cliente.isPresent() && username != null && !username.isEmpty()){
        clienteBd = cliente.get();
    }
        List<Articulo> copiasParaCalculo = articulos.stream()
                .map(Articulo::new)
                .collect(Collectors.toList());

    // Procesar precios usando tu método existente
    List<ArticuloDTOGet> articulosConPrecios = articuloService.procesarArticulos(
            copiasParaCalculo, clienteBd, cotizacion, null);
    String fecha = cotizacion.getFechaUltimaActualizacion()
    .toInstant()
    .atZone(ZoneId.of("America/Argentina/Buenos_Aires"))
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

pedidoAGuardar.setPrecioDolar("Valor Dolar al momento de la compra: " 
    + cotizacion.getPrecioVenta().toString() 
    + " ARS a la fecha - " 
    + fecha);
    // 6. Crear detalles del pedido con cantidades
    for (int i = 0; i < detalles.size(); i++) {
        DetallePedidoDTOPost detalleDto = detalles.get(i);
        ArticuloDTOGet articuloConPrecio = articulosConPrecios.get(i);
        
        DetallePedido detalle = new DetallePedido();
        detalle.setArticulo(articuloRepository.findById(detalleDto.idArticulo()).get());
        detalle.setCantidad(detalleDto.cantidad());
        
        // Usar el precio ya calculado por procesarArticulos
        if(articuloConPrecio.esOferta())
        {
            detalle.setPrecio_unitario(articuloConPrecio.precioOferta());
        }
        else{
            detalle.setPrecio_unitario(articuloConPrecio.preVtaPub1());
        }
        
        // Validar stock
        if (detalle.getArticulo().getStockActual() < detalle.getCantidad()) {
            throw new OutOfStockException("No hay stock suficiente para " + 
                detalle.getArticulo().getNombre());
        }
        
        // Actualizar stock
        Articulo articulobd = articuloRepository.findById(detalleDto.idArticulo()).get();
        articulobd.setStockActual(
                articulobd.getStockActual() - detalle.getCantidad());
        articuloRepository.save(articulobd);
        detallesAGuardar.add(detalle);
    }
    /////////////////////////// CUPON
        if(pedido.cuponDescuento() != null && !(clienteBd != null && clienteBd.getTipoCliente().name().equals("MAYORISTA"))){
            Optional<CuponDescuento> cupon = cuponDescuentoRepository.findByCodigo(pedido.cuponDescuento());
            if(cupon.isPresent()){
                if((LocalDate.now().isEqual(cupon.get().getFechaDesde()) || LocalDate.now().isAfter(cupon.get().getFechaDesde())) && (LocalDate.now().isEqual(cupon.get().getFechaHasta()) || LocalDate.now().isBefore(cupon.get().getFechaHasta())) && cupon.get().getCantidad() >= 0){
                    porcentajeDescuento = cupon.get().getPorcentajeDescuento();
                    maximoDescuento = cupon.get().getMontoMaximoDeDescuento();
                    for (DetallePedido detalle : detallesAGuardar) {

                        if(!detalle.getArticulo().isEsOferta()){
                            BigDecimal precioActual = detalle.getPrecio_unitario();

                            BigDecimal descuento = calcularDescuento(precioActual, new BigDecimal(String.valueOf(porcentajeDescuento)), maximoDescuento);

                            detalle.setPrecio_unitario(precioActual.subtract(descuento));
                        }
                    }
                    cupon.get().setCantidad(cupon.get().getCantidad() - 1);
                    cuponDescuentoRepository.save(cupon.get());
                }
            }
        }

        //Le seteo al pedido los detalles de pedido correspondientes
        pedidoAGuardar.setDetalles(detallesAGuardar);
        //Le seteo al pedido la fecha de creacion del mismo
        pedidoAGuardar.setFechaCreacion(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));

        //Creo un cambio de estado estableciendo como estado actual el estado creado
        PedidoCreado creado = new PedidoCreado();
        creado.setDescripcion("Pedido Creado");
        List<String> estadoPosibles = new ArrayList<>();
        estadoPosibles.add("pendienteDePago");
        estadoPosibles.add("cancelado");
        estadoPosibles.add("enPreparacion");
        creado.setEstadosPosibles(estadoPosibles);
        List<CambioEstado> ce = new ArrayList<>();
        CambioEstado cambioEstadoCreado = new CambioEstado();
        cambioEstadoCreado.setDescripcion("El pedido fue registrado con exito");
        cambioEstadoCreado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstadoCreado.setEstado(creado);
        cambioEstadoCreado.setFechaFin(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        ce.add(cambioEstadoCreado);

        pedidoAGuardar = pedidoRepository.save(pedidoAGuardar);

        TipoEnvio tipoEnvio = TipoEnvio.fromCodigo(pedido.tipoEnvio());
        if(tipoEnvio == TipoEnvio.ENVIOADOMICILIO){
            DTOCotizarEnvio dtoCotizar = new DTOCotizarEnvio(pedido.detalles(), pedido.cuponDescuento(), pedido.cliente().codigoPostal(), pedido.cliente().ciudad());
            EnvioConDatos armadoEnvio = cotizarEnvioService.cotizarEnvio(dtoCotizar, username);
            if(armadoEnvio.costoEnvio() != BigDecimal.ZERO){
                Articulo envioGeneral = new Articulo();
                Optional<Articulo> envioGeneralBd = articuloRepository.findArticulosByNombre("ENVIO GENERAL");
                if(envioGeneralBd.isPresent()){
                    envioGeneral = envioGeneralBd.get();
                }
                else{
                    envioGeneral.setId(Long.valueOf(99999999L));
                    envioGeneral.setNombre("ENVIO GENERAL");
                }
                DetallePedido detallePedidoEnvioGeneral = new DetallePedido();
                detallePedidoEnvioGeneral.setArticulo(envioGeneral);
                detallePedidoEnvioGeneral.setCantidad(1);
                detallePedidoEnvioGeneral.setPrecio_unitario(armadoEnvio.costoEnvio().setScale(2,RoundingMode.HALF_UP));
                pedidoAGuardar.getDetalles().add(detallePedidoEnvioGeneral);
            }
            
            List<Envio> envios  = new ArrayList<>();
            for (ArmarEnvioConCosto bulto : armadoEnvio.bultos()) {
                Envio envio = new Envio();
                List<Articulo> articulosEnvio = new ArrayList<>();
                for (ArticuloItem articulo : bulto.articulosAEmpaquetar()) {
                    articulosEnvio.add(articulo.articulo());
                }
                envio.setArticulos(articulosEnvio);
                envio.setCostoEnvio(bulto.costoBulto());
                envio.setDireccion(pedidoAGuardar.getDireccion());
                envios.add(envio);
            }
            
            pedidoAGuardar.setEnvios(envios);
        }
        //De acuerdo con la forma de pago elegida creo un pago estableciendo como tipo de pago lo que selecciono el cliente
        TipoPago tipoPago = TipoPago.fromCodigo(pedido.formaPago());
        switch(tipoPago){
            case EFECTIVO:
                Pago pagoEfectivo = new Pago();
                pagoEfectivo.setTipoPago(tipoPago);
                pagoEfectivo.setTotal(pedidoAGuardar.calcularTotal());
                pagoEfectivo = pagoService.save(pagoEfectivo);
                pedidoAGuardar.setPago(pagoEfectivo);
                break;
            case TRANSFERENCIA:
                Pago pagoTransferencia = new Pago();    
                pagoTransferencia.setTipoPago(tipoPago);
                pagoTransferencia.setTotal(pedidoAGuardar.calcularTotal());
                List<DatosTransferencia> datosTransferencias = datosTransferenciaRepository.findAll();
                pagoTransferencia.setDatosTransferencia(datosTransferencias.getFirst());
                pagoTransferencia = pagoService.save(pagoTransferencia);
                pedidoAGuardar.setPago(pagoTransferencia);
                break;
            case NAVE:
                ResponsePago responsePago = naveService.generarIntencionDePago(pedidoAGuardar, pedido.esCelular());
                pedidoConPago.setResponsePago(responsePago);
                Pago pagoNave = new Pago();
                pagoNave.setTipoPago(tipoPago);
                pagoNave.setTotal(pedidoAGuardar.calcularTotal());
                pagoNave = pagoService.save(pagoNave);
                pagoNave.setCodigoTransaccion(responsePago.getData().getTransaction_id());
                pagoNave.setLinkDePago(responsePago.getData().getCheckout_url());
                pedidoAGuardar.setPago(pagoNave);
        }

        //De acuerdo al tipo de envio le establezco un nuevo estado
        switch(tipoEnvio){
            case ENVIOADOMICILIO:
                PedidoPendienteDePago pendienteDePago = new PedidoPendienteDePago();
                pendienteDePago.setDescripcion("Pedido Pendiente De Pago");
                List<String> estadoPosiblePendientePago = new ArrayList<>();
                estadoPosiblePendientePago.add("pagado");
                estadoPosiblePendientePago.add("rechazado");
                estadoPosiblePendientePago.add("cancelado");
                pendienteDePago.setEstadosPosibles(estadoPosiblePendientePago);
                CambioEstado cambioEstadoPendienteDePago = new CambioEstado();
                cambioEstadoPendienteDePago.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                cambioEstadoPendienteDePago.setEstado(pendienteDePago);
                String descripcion = "";
                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.TRANSFERENCIA){     
                    descripcion = descripcion + "\n" + "CBU: " + pedidoAGuardar.getPago().getDatosTransferencia().getCbu() + "\n" + "ALIAS: " + pedidoAGuardar.getPago().getDatosTransferencia().getAlias() + "\n" + "TITULAR DE LA CUENTA: " + pedidoAGuardar.getPago().getDatosTransferencia().getTitular() + "\n" + "BANCO: " + pedidoAGuardar.getPago().getDatosTransferencia().getBanco();
                }
                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.NAVE){
                    descripcion = "Si aun no realizo su pago ingrese a: " + pedidoConPago.getResponsePago().getData().getCheckout_url();
                }    
                cambioEstadoPendienteDePago.setDescripcion(descripcion);
                ce.add(cambioEstadoPendienteDePago);
                pedidoAGuardar.setCambiosEstado(ce);
                pedidoAGuardar.setEstadoActual(pendienteDePago);
                pedidoAGuardar.setTipoEnvio(tipoEnvio);
                
            case RETIROENLOCAL:
                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.EFECTIVO){
                    PedidoEnPreparacion enPreparacion = new PedidoEnPreparacion();
                    enPreparacion.setDescripcion("Pedido En Preparacion");
                    List<String> estadoPosibleEnPreparacion = new ArrayList<>();
                    estadoPosibleEnPreparacion.add("listoParaEntregar");
                    estadoPosibleEnPreparacion.add("cancelado");
                    estadoPosibleEnPreparacion.add("rechazado");
                    enPreparacion.setEstadosPosibles(estadoPosibleEnPreparacion);
                    CambioEstado cambioEstadoEnPreparacion = new CambioEstado();
                    cambioEstadoEnPreparacion.setDescripcion("El pedido se encuentra actualmente en preparacion, pronto estara listo para ser retirado");
                    cambioEstadoEnPreparacion.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                    cambioEstadoEnPreparacion.setEstado(enPreparacion);
                    ce.add(cambioEstadoEnPreparacion);
                    pedidoAGuardar.setCambiosEstado(ce);
                    pedidoAGuardar.setEstadoActual(enPreparacion);
                    pedidoAGuardar.setTipoEnvio(tipoEnvio);
                    break;
                }
                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.NAVE || pedidoAGuardar.getPago().getTipoPago() == TipoPago.TRANSFERENCIA){
                    PedidoPendienteDePago pendienteDePagoRetiroLocal = new PedidoPendienteDePago();
                    pendienteDePagoRetiroLocal.setDescripcion("Pedido Pendiente De Pago");
                    List<String> estadosPosiblesPendientePagoNave = new ArrayList<>();
                    estadosPosiblesPendientePagoNave.add("pagado");
                    estadosPosiblesPendientePagoNave.add("rechazado");
                    estadosPosiblesPendientePagoNave.add("cancelado");
                    pendienteDePagoRetiroLocal.setEstadosPosibles(estadosPosiblesPendientePagoNave);
                    CambioEstado cambioEstadoPendienteDePagoRetiroLocal = new CambioEstado();
                    cambioEstadoPendienteDePagoRetiroLocal.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                    cambioEstadoPendienteDePagoRetiroLocal.setEstado(pendienteDePagoRetiroLocal);
                    if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.NAVE){
                        String descripcionPendienteDePagoRetiroLocal = "Si aun no se realizo un pago ingrese a: " + pedidoConPago.getResponsePago().getData().getCheckout_url();
                        cambioEstadoPendienteDePagoRetiroLocal.setDescripcion(descripcionPendienteDePagoRetiroLocal);
                    }
                    else{
                        String descripcionPendienteDePagoRetiroLocal = "CBU: " + pedidoAGuardar.getPago().getDatosTransferencia().getCbu() + "\n" + "ALIAS: " + pedidoAGuardar.getPago().getDatosTransferencia().getAlias() + "\n" + "TITULAR DE LA CUENTA: " + pedidoAGuardar.getPago().getDatosTransferencia().getTitular() + "\n" + "BANCO: " + pedidoAGuardar.getPago().getDatosTransferencia().getBanco();
                        cambioEstadoPendienteDePagoRetiroLocal.setDescripcion(descripcionPendienteDePagoRetiroLocal);  
                    }
                    
                    ce.add(cambioEstadoPendienteDePagoRetiroLocal);
                    pedidoAGuardar.setCambiosEstado(ce);
                    pedidoAGuardar.setEstadoActual(pendienteDePagoRetiroLocal);
                    pedidoAGuardar.setTipoEnvio(tipoEnvio);
                    break;
                }
        } 
        //Guardo el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedidoAGuardar);
        //Envio el mail
        correoService.enviarEmailEstadoPedido(pedidoGuardado.getId());
        correoService.enviarEmailEstadoPedidoAdministracion(pedidoGuardado.getId());
        //Retorno los datos al front
        pedidoConPago.setPedido(pedidoGuardado);
        return pedidoConPago;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) throws NoSuchElementException {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if(pedido.isPresent()){
            List<DetallePedido> detalles = pedido.get().getDetalles();
            for (DetallePedido detallePedido : detalles) {
                detallePedidoRepository.deleteById(detallePedido.getId());
            }
            pedidoRepository.deleteById(id);
        }
        else{
            throw new NoSuchElementException("No se encuentra el pedido con el id: " + pedido.get().getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PedidoDTOGet> findByCliente(Long clienteId) throws Exception {
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isPresent()){
            List<PedidoDTOGet> pedidosAMostrar = new ArrayList<>();
            List<Pedido> pedidos = pedidoRepository.findByCliente(clienteId);
            for (Pedido pedido : pedidos) {
                Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
                .filter(CambioEstado::esActual)
                .findFirst();
                PedidoDTOGet pedidoDTO = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
                pedidosAMostrar.add(pedidoDTO);
            }
            return pedidosAMostrar;
        }
        else{
            throw new NoSuchElementException("No se encontro registrado un cliente con id: " + clienteId);
        }
    }

    @Override
    public PedidoDTOGet pedidoCreado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoPendienteDePago(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoPagado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        for (int i = pedido.getCambiosEstado().size() - 1; i >= 0; i--) {
            if(pedido.getCambiosEstado().get(i).getEstado() instanceof PedidoRechazado){
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Optional<Articulo> articuloBd = articuloRepository.findById(detalle.getArticulo().getId());
                    if(articuloBd.isPresent()){
                        Articulo articulo = articuloBd.get();
                        Integer stockActual = articulo.getStockActual();
                        stockActual -= detalle.getCantidad();
                        articulo.setStockActual(stockActual);
                        articuloRepository.save(articulo);
                    }
                }
                break;
            }
        }
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoEnPreparacion(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoListoParaEntregar(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoDespachado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoEntregado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoCancelado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        for (DetallePedido detalles : pedido.getDetalles()) {
            Optional<Articulo> articulo = articuloRepository.findById(detalles.getArticulo().getId());
            if(articulo.isPresent()){
                Articulo articuloBd = articulo.get();
                Integer stockActual = articuloBd.getStockActual();
                stockActual += detalles.getCantidad();
                articuloBd.setStockActual(stockActual);
                articuloRepository.save(articulo.get());
            }
        }
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoRechazado(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        for (DetallePedido detalles : pedido.getDetalles()) {
            Optional<Articulo> articulo = articuloRepository.findById(detalles.getArticulo().getId());
            if(articulo.isPresent()){
                Articulo articuloBd = articulo.get();
                Integer stockActual = articuloBd.getStockActual();
                stockActual += detalles.getCantidad();
                articuloBd.setStockActual(stockActual);
                articuloRepository.save(articulo.get());
            }
        }
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet saveCambioEstado(Pedido pedido) throws Exception {
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        Optional<CambioEstado> ultimoCE = pedidoGuardado.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedidoGuardado.getId(), pedidoGuardado.getCliente(), pedidoGuardado.getFechaCreacion(), pedidoGuardado.getDetalles(), pedidoGuardado.calcularTotal(),pedidoGuardado.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get());
        return pedidoAMostrar;
    }

    @Override
    public List<Pedido> findByFiltro(LocalDate fechaInicio, LocalDate fechaFin, String estado, String documento) {
        return pedidoRepository.findPedidosByFilters(fechaInicio, fechaFin, estado, documento);
    }

    @Override
    public Optional<Pedido> obtenerEstados(EstadosGetDTO datos) throws NoSuchElementException {
        Optional<Cliente> clienteBd = clienteRepository.findByDocumento(datos.documentoCliente());
        if(clienteBd.isPresent()){
            Cliente cliente = clienteBd.get();
            Optional<Pedido> pedido = pedidoRepository.findById(datos.idPedido());
            if(pedido.isPresent()){
                if(pedido.get().getCliente().getId() == cliente.getId()){
                    return pedido;
                }
                else{
                    throw new NoSuchElementException("El pedido indicado no pertenece al cliente especificado");
                }
            }
            else{
                throw new NoSuchElementException("No existe el pedido con id: " + datos.idPedido());
            }
        }
        else{
            throw new NoSuchElementException("No existe un cliente registrado con el documento: " + datos.documentoCliente());
        }
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }
}
