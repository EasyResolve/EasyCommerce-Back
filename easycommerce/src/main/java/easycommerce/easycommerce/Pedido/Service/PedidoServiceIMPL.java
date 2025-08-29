package easycommerce.easycommerce.Pedido.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.lang.classfile.ClassFile.Option;
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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOGet;
import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Articulo.Repository.ArticuloRepository;
import easycommerce.easycommerce.Articulo.Service.ArticuloService;
import easycommerce.easycommerce.Caja.Repository.CajaRepository;
import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cliente.Model.TipoCliente;
import easycommerce.easycommerce.Cliente.Repository.ClienteRepository;
import easycommerce.easycommerce.CodigoPostalZona.Repository.CodigoPostalZonaRepository;
import easycommerce.easycommerce.Correo.CorreoService;
import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Cotizacion.Repository.CotizacionRepository;
import easycommerce.easycommerce.Cotizacion.Service.CotizacionService;
import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.CuponDescuento.Repository.CuponDescuentoRepository;
import easycommerce.easycommerce.Datos.Model.DatosTransferencia;
import easycommerce.easycommerce.Datos.Repository.DatosTransferenciaRepository;
import easycommerce.easycommerce.DetallePedido.DTOs.DetallePedidoDTOPost;
import easycommerce.easycommerce.DetallePedido.Model.DetallePedido;
import easycommerce.easycommerce.DetallePedido.Repository.DetallePedidoRepository;
import easycommerce.easycommerce.Direccion.Model.Direccion;
import easycommerce.easycommerce.Direccion.Service.DireccionService;
import easycommerce.easycommerce.Envio.Service.CotizarEnvioService;
import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Estados.CambioEstado.Repository.CambioEstadoRepository;
import easycommerce.easycommerce.Estados.Estado.Repository.EstadoRepository;
import easycommerce.easycommerce.Estados.EstadoPedido.Model.EstadoPedido;
import easycommerce.easycommerce.Estados.EstadoPedido.Model.PedidoEnPreparacion;
import easycommerce.easycommerce.Estados.EstadoPedido.Model.PedidoPendienteDePago;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.OutOfStockException;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;
import easycommerce.easycommerce.Pago.Model.Pago;
import easycommerce.easycommerce.Pago.Model.TipoPago;
import easycommerce.easycommerce.Pago.Service.PagoService;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMinorista;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;
import easycommerce.easycommerce.Pedido.DTOs.EstadosGetDTO;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGet;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOGetADMIN;
import easycommerce.easycommerce.Pedido.DTOs.PedidoDTOPost;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Model.PedidoConPago;
import easycommerce.easycommerce.Pedido.Model.TipoEnvio;
import easycommerce.easycommerce.Pedido.Repository.PedidoRepository;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;

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
    private final DatosTransferenciaRepository datosTransferenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParametroRepository parametroRepository;
    private final CuponDescuentoRepository cuponDescuentoRepository;
    private final ArticuloService articuloService;
    public PedidoServiceIMPL(PedidoRepository pedidoRepository, ArticuloRepository articuloRepository,
            ClienteRepository clienteRepository, DetallePedidoRepository detallePedidoRepository,
            CotizacionRepository cotizacionRepository, CotizacionService cotizacionService, PagoService pagoService,
            CambioEstadoRepository cambioEstadoRepository, EstadoRepository estadoRepository,
            CorreoService correoService, DireccionService direccionService, DatosTransferenciaRepository datosTransferenciaRepository, UsuarioRepository usuarioRepository, ParametroRepository parametroRepository,
            CuponDescuentoRepository cuponDescuentoRepository,
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
        this.datosTransferenciaRepository = datosTransferenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository = parametroRepository;
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
            pedido.getEstadoActual().getEstadosPosibles();
            PedidoDTOGetADMIN pedidoDTO = new PedidoDTOGetADMIN(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(), pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
            pedido.get().getEstadoActual().getEstadosPosibles();
            PedidoDTOGet pedidoDTO = new PedidoDTOGet(pedido.get().getId(), pedido.get().getCliente(), pedido.get().getFechaCreacion(), pedido.get().getDetalles(), pedido.get().calcularTotal(),pedido.get().getEstadoActual().getEstado(),pedido.get().getPago(), pedido.get().getTipoEnvio(), pedido.get().getEstadoActual(), pedido.get().getEnvios(), ultimoCE.get(), pedido.get().getPrecioDolar(), pedido.get().getListaCliente());
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
        PedidoPendienteDePago creado = new PedidoPendienteDePago();
        creado.setDescripcion("Pedido Creado");
        List<CambioEstado> ce = new ArrayList<>();
        CambioEstado cambioEstadoCreado = new CambioEstado();
        cambioEstadoCreado.setDescripcion("El pedido fue registrado con exito");
        cambioEstadoCreado.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        cambioEstadoCreado.setEstado(creado);
        cambioEstadoCreado.setFechaFin(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        ce.add(cambioEstadoCreado);

        pedidoAGuardar = pedidoRepository.save(pedidoAGuardar);

        TipoEnvio tipoEnvio = TipoEnvio.fromCodigo(pedido.tipoEnvio());
        /*if(tipoEnvio == TipoEnvio.ENVIOADOMICILIO){
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
            */

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
            /*case NAVE:
                ResponsePago responsePago = naveService.generarIntencionDePago(pedidoAGuardar, pedido.esCelular());
                pedidoConPago.setResponsePago(responsePago);
                Pago pagoNave = new Pago();
                pagoNave.setTipoPago(tipoPago);
                pagoNave.setTotal(pedidoAGuardar.calcularTotal());
                pagoNave = pagoService.save(pagoNave);
                pagoNave.setCodigoTransaccion(responsePago.getData().getTransaction_id());
                pagoNave.setLinkDePago(responsePago.getData().getCheckout_url());
                pedidoAGuardar.setPago(pagoNave);
            */
        }

        //De acuerdo al tipo de envio le establezco un nuevo estado
        switch(tipoEnvio){
            /*case ENVIOADOMICILIO:
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
            */
                
            case RETIROENLOCAL:
                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.EFECTIVO){
                    PedidoEnPreparacion enPreparacion = new PedidoEnPreparacion();
                    enPreparacion.setDescripcion("Pedido En Preparacion");
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

                if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.TRANSFERENCIA){
                    PedidoPendienteDePago pendienteDePagoRetiroLocal = new PedidoPendienteDePago();
                    pendienteDePagoRetiroLocal.setDescripcion("Pedido Pendiente De Pago");
                    CambioEstado cambioEstadoPendienteDePagoRetiroLocal = new CambioEstado();
                    cambioEstadoPendienteDePagoRetiroLocal.setFechaInicio(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                    cambioEstadoPendienteDePagoRetiroLocal.setEstado(pendienteDePagoRetiroLocal);
                    /*if(pedidoAGuardar.getPago().getTipoPago() == TipoPago.NAVE){
                        String descripcionPendienteDePagoRetiroLocal = "Si aun no se realizo un pago ingrese a: " + pedidoConPago.getResponsePago().getData().getCheckout_url();
                        cambioEstadoPendienteDePagoRetiroLocal.setDescripcion(descripcionPendienteDePagoRetiroLocal);
                    }
                    else{
                        String descripcionPendienteDePagoRetiroLocal = "CBU: " + pedidoAGuardar.getPago().getDatosTransferencia().getCbu() + "\n" + "ALIAS: " + pedidoAGuardar.getPago().getDatosTransferencia().getAlias() + "\n" + "TITULAR DE LA CUENTA: " + pedidoAGuardar.getPago().getDatosTransferencia().getTitular() + "\n" + "BANCO: " + pedidoAGuardar.getPago().getDatosTransferencia().getBanco();
                        cambioEstadoPendienteDePagoRetiroLocal.setDescripcion(descripcionPendienteDePagoRetiroLocal);  
                    }
                    */
                    String descripcionPendienteDePagoRetiroLocal = "CBU: " + pedidoAGuardar.getPago().getDatosTransferencia().getCbu() + "\n" + "ALIAS: " + pedidoAGuardar.getPago().getDatosTransferencia().getAlias() + "\n" + "TITULAR DE LA CUENTA: " + pedidoAGuardar.getPago().getDatosTransferencia().getTitular() + "\n" + "BANCO: " + pedidoAGuardar.getPago().getDatosTransferencia().getBanco();
                        cambioEstadoPendienteDePagoRetiroLocal.setDescripcion(descripcionPendienteDePagoRetiroLocal);  
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
                pedido.getEstadoActual().getEstadosPosibles();
                PedidoDTOGet pedidoDTO = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(),pedido.getPrecioDolar(), pedido.getListaCliente());
                pedidosAMostrar.add(pedidoDTO);
            }
            return pedidosAMostrar;
        }
        else{
            throw new NoSuchElementException("No se encontro registrado un cliente con id: " + clienteId);
        }
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
        return pedidoAMostrar;
    }

    @Override
    public PedidoDTOGet pedidoEnCamino(Pedido pedido) throws Exception {
        List<CambioEstado> ce = cambioEstadoRepository.saveAll(pedido.getCambiosEstado());
        EstadoPedido estadoActual = estadoRepository.save(pedido.getEstadoActual());
        pedido.setCambiosEstado(ce);
        pedido.setEstadoActual(estadoActual);
        pedido = pedidoRepository.save(pedido);
        correoService.enviarEmailEstadoPedido(pedido.getId());
        Optional<CambioEstado> ultimoCE = pedido.getCambiosEstado().stream()
        .filter(CambioEstado::esActual)
        .findFirst();
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedido.getId(), pedido.getCliente(), pedido.getFechaCreacion(), pedido.getDetalles(), pedido.calcularTotal(),pedido.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        PedidoDTOGet pedidoAMostrar = new PedidoDTOGet(pedidoGuardado.getId(), pedidoGuardado.getCliente(), pedidoGuardado.getFechaCreacion(), pedidoGuardado.getDetalles(), pedidoGuardado.calcularTotal(),pedidoGuardado.getEstadoActual().getEstado(), pedido.getPago(), pedido.getTipoEnvio(), pedido.getEstadoActual(), pedido.getEnvios(), ultimoCE.get(), pedido.getPrecioDolar(), pedido.getListaCliente());
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
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        pedido.get().getEstadoActual().getEstadosPosibles();
        return pedido;
    }
}
