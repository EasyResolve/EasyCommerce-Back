package easycommerce.easycommerce.Envio.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOGet;
import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Articulo.Repository.ArticuloRepository;
import easycommerce.easycommerce.Articulo.Service.ArticuloService;
import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.CodigoPostalZona.Model.CodigoPostalZona;
import easycommerce.easycommerce.CodigoPostalZona.Repository.CodigoPostalZonaRepository;
import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Cotizacion.Repository.CotizacionRepository;
import easycommerce.easycommerce.Cotizacion.Service.CotizacionService;
import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.CuponDescuento.Repository.CuponDescuentoRepository;
import easycommerce.easycommerce.DetallePedido.DTOs.DetallePedidoDTOPost;
import easycommerce.easycommerce.Envio.DTOs.DTOCotizarEnvio;
import easycommerce.easycommerce.Envio.Model.ArmarEnvio;
import easycommerce.easycommerce.Envio.Model.ArmarEnvioConCosto;
import easycommerce.easycommerce.Envio.Model.ArticuloItem;
import easycommerce.easycommerce.Envio.Model.EnvioConDatos;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;
import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;
import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;
import easycommerce.easycommerce.TablaCotizacion.Repository.CotizacionEnvioRepository;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;

@Service
public class CotizarEnvioService {
    
    private final ArticuloRepository articuloRepository;
    private final ParametroRepository parametroRepository;
    private final CodigoPostalZonaRepository codigoPostalZonaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final CuponDescuentoRepository cuponDescuentoRepository;
    private final CotizacionService cotizacionService;
    private final CotizacionEnvioRepository cotizacionEnvioRepository;
    private final ArticuloService articuloService;

    public CotizarEnvioService(ArticuloRepository articuloRepository, ParametroRepository parametroRepository,
                               CodigoPostalZonaRepository codigoPostalZonaRepository, UsuarioRepository usuarioRepository,
                               CotizacionRepository cotizacionRepository, CuponDescuentoRepository cuponDescuentoRepository,
                               CotizacionService cotizacionService, CotizacionEnvioRepository cotizacionEnvioRepository,
                               ArticuloService articuloService) {
        this.articuloRepository = articuloRepository;
        this.parametroRepository = parametroRepository;
        this.codigoPostalZonaRepository = codigoPostalZonaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.cuponDescuentoRepository = cuponDescuentoRepository;
        this.cotizacionService = cotizacionService;
        this.cotizacionEnvioRepository = cotizacionEnvioRepository;
        this.articuloService = articuloService;
    }


    public EnvioConDatos cotizarEnvio(DTOCotizarEnvio cotizarEnvio, String username) throws Exception{
        //Inicializacion de variables
        List<ArmarEnvio> bultos = new ArrayList<>();
        List<ArticuloItem> articulosAEmpaquetar = new ArrayList<>();
        List<ArmarEnvioConCosto> bultosConCosto = new ArrayList<>();
        Cliente cliente = null;
        BigDecimal costoEnvio = BigDecimal.valueOf(0);
        BigDecimal costoEnvioGeneral = BigDecimal.valueOf(0);
        BigDecimal montoPedido = BigDecimal.valueOf(0);
        Double porcentajeDescuento = 0.0;
        BigDecimal maximoDescuento = BigDecimal.valueOf(0);

        //variable
        Double pesoTotal = 0.0;
        Double volumenTotal = 0.0;
        List<ArticuloItem> articulosBulto = new ArrayList<>();
        
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
            else{
                throw new NoSuchElementException("No se encontro un usuario con el username: " + username);
            }
        }

        for (DetallePedidoDTOPost detalle : cotizarEnvio.detalles()) {
            //Obtengo el articulo de la base de datos
            Optional<Articulo> articuloBd = articuloRepository.findById(detalle.idArticulo()).map(Articulo::new);
            if(articuloBd.isPresent()){
                Articulo articulo = articuloBd.get();
                //Creo los items a enviar
                for (int i = 0; i < detalle.cantidad(); i++) {
                    ArticuloItem articuloAEmpaquetar = new ArticuloItem(articulo, articulo.obtenerVolumen());
                    articulosAEmpaquetar.add(articuloAEmpaquetar);
                }
            }
        }
        CuponDescuento cuponDescuento = null;
        if(cotizarEnvio.cuponDescuento() != null && !cotizarEnvio.cuponDescuento().isEmpty()){
            Optional<CuponDescuento> cupon = cuponDescuentoRepository.findByCodigo(cotizarEnvio.cuponDescuento());
            if(cupon.isPresent()){
                cuponDescuento = cupon.get();
            }
        }



        List<Articulo> preciosDolares = articulosAEmpaquetar.stream().map(articuloItem -> articuloItem.articulo()).collect(Collectors.toList());
        Cotizacion cotizacionDolar = cotizacionRepository.findByMoneda("USD")
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
        if(cliente != null && username != null&& !username.isEmpty()){
            clienteBd = cliente;
        }



        List<ArticuloDTOGet> preciosPesos = this.articuloService.procesarArticulos(preciosDolares,clienteBd, cotizacionDolar, cuponDescuento);
        montoPedido = preciosPesos.stream()
                .map(a -> a.esOferta() && a.fechaDesdeOferta() != null && (LocalDate.now().isEqual(LocalDate.parse(a.fechaDesdeOferta())) || LocalDate.now().isAfter(LocalDate.parse(a.fechaDesdeOferta()))) && a.fechaHastaOferta() != null && (LocalDate.now().isEqual(LocalDate.parse(a.fechaHastaOferta())) || LocalDate.now().isBefore(LocalDate.parse(a.fechaHastaOferta()))) ? a.precioOferta() : a.preVtaPub1())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (ArticuloItem articulo : articulosAEmpaquetar) {
            if ((pesoTotal + (articulo.articulo().getPeso())) <= 25000) {
                //Calculo el peso y el volumen de esos items para el envio
                pesoTotal = pesoTotal + (articulo.articulo().getPeso());
                volumenTotal = volumenTotal + (articulo.articulo().obtenerVolumen());
                articulosBulto.add(articulo);
            } else {

                ArmarEnvio bulto = new ArmarEnvio(pesoTotal, volumenTotal, articulosBulto);
                bultos.add(bulto);
                pesoTotal = 0.0;
                volumenTotal = 0.0;
                articulosBulto = new ArrayList<>();

                if ((pesoTotal + (articulo.articulo().getPeso()) <= 25000)) {
                    pesoTotal = pesoTotal + (articulo.articulo().getPeso());
                    volumenTotal = volumenTotal + (articulo.articulo().obtenerVolumen());
                    articulosBulto.add(articulo);
                } else {
                    throw new Exception("No se puede empaquetar el articulo porque pesa mas de 25Kg");
                }
            }

            //Buscar la cotizacion del dolar
        }
        ArmarEnvio bulto1 = new ArmarEnvio(pesoTotal, volumenTotal, articulosAEmpaquetar);
        bultos.add(bulto1);

        Optional<Parametro> montoEnvioGratis = parametroRepository.findByDescripcion("montoEnvioGratisGeneral");

        if (montoEnvioGratis.isPresent()) {
            String valor = montoEnvioGratis.get().getValor();
            if(cotizarEnvio.ciudad().equals("Jovita") && cotizarEnvio.codigoPostal().equals("6127")){
                costoEnvio = BigDecimal.ZERO;
                costoEnvioGeneral = BigDecimal.ZERO;
                for (ArmarEnvio bulto : bultos) {
                    ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                        bulto.pesoTotal(),
                        bulto.volumenTotal(),
                        bulto.articulosAEmpaquetar(),
                        costoEnvio
                    );
                    bultosConCosto.add(envioConCosto);
                }
            }
            else if (Integer.parseInt(valor) == -1 || montoPedido.compareTo(BigDecimal.valueOf(Long.valueOf(valor))) <= 0){
                
                Optional<CodigoPostalZona> codigoPostal = codigoPostalZonaRepository.findAll()
                    .stream()
                    .filter(zona -> zona.getCodigoPostal().equals(cotizarEnvio.codigoPostal()) &&
                                    zona.getCiudad().equals(cotizarEnvio.ciudad()))
                    .findFirst();

                if (codigoPostal.isPresent()) {
                    Optional<Parametro> costoEnvioBd = parametroRepository.findByDescripcion("montoEnvioZonas");
                    if (costoEnvioBd.isPresent()) {
                        costoEnvioGeneral = BigDecimal.valueOf(Double.parseDouble(costoEnvioBd.get().getValor()));
                        costoEnvio = BigDecimal.valueOf(Double.parseDouble(costoEnvioBd.get().getValor())).divide(BigDecimal.valueOf(bultos.size()),2,RoundingMode.HALF_UP);
                        for (ArmarEnvio bulto : bultos) {
                            ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                            bulto.pesoTotal(),
                            bulto.volumenTotal(),
                            bulto.articulosAEmpaquetar(),
                            costoEnvio
                            );
                            bultosConCosto.add(envioConCosto);
                        }
                    } else {
                        throw new NoSuchElementException("No se encontró un precio para la ciudad y código postal indicado");
                    }
                } else {
                    for (ArmarEnvio bulto : bultos) {
                        pesoTotal = 0.0;
                        costoEnvio = BigDecimal.ZERO;
                        pesoTotal += bulto.pesoTotal();

                        Double pesoVolumetrico = pesoTotal / 6000;
                        Double pesoCotizar = Math.max(pesoTotal, pesoVolumetrico);

                        Optional<CotizacionEnvio> cotizacion = cotizacionEnvioRepository.findByEntreValores(pesoCotizar);
                        if (cotizacion.isPresent()) {
                            BigDecimal monto = cotizacion.get().getMontoEnvio();
                            costoEnvioGeneral = costoEnvioGeneral.add(monto);
                            costoEnvio = costoEnvio.add(monto);

                            ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                                bulto.pesoTotal(),
                                bulto.volumenTotal(),
                                bulto.articulosAEmpaquetar(),
                                costoEnvio
                            );
                            bultosConCosto.add(envioConCosto);
                        } else {
                            throw new Exception("No se encuentra una cotización para el peso de su pedido");
                        }
                    }
                }
            } else {
                costoEnvio = BigDecimal.ZERO;
                costoEnvioGeneral = BigDecimal.ZERO;

                for (ArmarEnvio bulto : bultos) {
                    pesoTotal = 0.0;
                    pesoTotal += bulto.pesoTotal();

                    ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                            bulto.pesoTotal(),
                            bulto.volumenTotal(),
                            bulto.articulosAEmpaquetar(),
                            costoEnvio
                    );
                    bultosConCosto.add(envioConCosto);
                }
            }

        } else {
            Optional<CodigoPostalZona> codigoPostal = codigoPostalZonaRepository.findAll()
                .stream()
                .filter(zona -> zona.getCodigoPostal().equals(cotizarEnvio.codigoPostal()) &&
                                zona.getCiudad().equals(cotizarEnvio.ciudad()))
                .findFirst();

            if(cotizarEnvio.ciudad().equals("Jovita") && cotizarEnvio.codigoPostal().equals("6127")){
                costoEnvio = BigDecimal.ZERO;
                costoEnvioGeneral = BigDecimal.ZERO;
                for (ArmarEnvio bulto : bultos) {
                    ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                        bulto.pesoTotal(),
                        bulto.volumenTotal(),
                        bulto.articulosAEmpaquetar(),
                        costoEnvio
                    );
                    bultosConCosto.add(envioConCosto);
                }
            }
            else if (codigoPostal.isPresent()) {
                Optional<Parametro> costoEnvioBd = parametroRepository.findByDescripcion("montoEnvioZonas");
                if (costoEnvioBd.isPresent()) {
                    costoEnvioGeneral = BigDecimal.valueOf(Double.parseDouble(costoEnvioBd.get().getValor()));
                    costoEnvio = BigDecimal.valueOf(Double.parseDouble(costoEnvioBd.get().getValor()));
                    for (ArmarEnvio bulto : bultos) {
                        ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                            bulto.pesoTotal(),
                            bulto.volumenTotal(),
                            bulto.articulosAEmpaquetar(),
                            costoEnvio
                        );
                        bultosConCosto.add(envioConCosto);
                    }
                } else {
                    throw new NoSuchElementException("No se encontró un precio para la ciudad y código postal indicado");
                }
            } else {
                pesoTotal = 0.0;
                volumenTotal = 0.0;
                costoEnvio = BigDecimal.ZERO;
                articulosBulto = new ArrayList<>();

                for (ArmarEnvio bulto : bultos) {
                    pesoTotal += bulto.pesoTotal();
                    Double pesoVolumetrico = pesoTotal / 6000;
                    Double pesoCotizar = Math.max(pesoTotal, pesoVolumetrico);

                    Optional<CotizacionEnvio> cotizacion = cotizacionEnvioRepository.findByEntreValores(pesoCotizar);
                    if (cotizacion.isPresent()) {
                        BigDecimal monto = cotizacion.get().getMontoEnvio();
                        costoEnvioGeneral = costoEnvioGeneral.add(monto);
                        costoEnvio = costoEnvio.add(monto);

                        ArmarEnvioConCosto envioConCosto = new ArmarEnvioConCosto(
                            bulto.pesoTotal(),
                            bulto.volumenTotal(),
                            bulto.articulosAEmpaquetar(),
                            costoEnvio
                        );
                        bultosConCosto.add(envioConCosto);
                    } else {
                        throw new Exception("No se encuentra una cotización para el peso de su pedido");
                    }
                }
            }
        }

        EnvioConDatos datosEnvio = new EnvioConDatos(bultosConCosto, costoEnvioGeneral);
        return datosEnvio;
    }
}

