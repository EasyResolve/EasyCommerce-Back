package easycommerce.easycommerce.Articulo.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOActualizacion;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOGet;
import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Articulo.Repository.ArticuloRepository;
import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.Cotizacion.Service.CotizacionService;
import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.CuponDescuento.Repository.CuponDescuentoRepository;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.NotFinalPriceException;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;
import easycommerce.easycommerce.ImagenePorArticulo.Models.ImagenesPorArticulo;
import easycommerce.easycommerce.ImagenePorArticulo.Repository.ImagenesPorArticuloRepository;
import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Marca.Repository.MarcaRepository;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMayorista;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMinorista;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;
import easycommerce.easycommerce.Rubro.Model.Rubro;
import easycommerce.easycommerce.Rubro.Repository.RubroRepository;
import easycommerce.easycommerce.SubRubro.Model.SubRubro;
import easycommerce.easycommerce.SubRubro.Repository.SubRubroRepository;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOPost;
import easycommerce.easycommerce.Articulo.DTOs.IsDestacado;
import easycommerce.easycommerce.Articulo.Model.ArticuloConImagenes;
import easycommerce.easycommerce.Articulo.Model.ArticuloConImagenesYErrores;

@Service
public class ArticuloServiceIMPL implements ArticuloService{

    private final ArticuloRepository articuloRepository;
    private final RubroRepository rubroRepository;
    private final CotizacionService cotizacionService;
    private final SubRubroRepository subRubroRepository;
    private final MarcaRepository marcaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParametroRepository parametroRepository;
    private final CuponDescuentoRepository cuponDescuentoRepository;
    private final ImagenesPorArticuloRepository imagenesPorArticuloRepository;

    public ArticuloServiceIMPL(ArticuloRepository articuloRepository, RubroRepository rubroRepository,
            CotizacionService cotizacionService, SubRubroRepository subRubroRepository, MarcaRepository marcaRepository,
            UsuarioRepository usuarioRepository, ParametroRepository parametroRepository,
            CuponDescuentoRepository cuponDescuentoRepository,
            ImagenesPorArticuloRepository imagenesPorArticuloRepository) {
        this.articuloRepository = articuloRepository;
        this.rubroRepository = rubroRepository;
        this.cotizacionService = cotizacionService;
        this.subRubroRepository = subRubroRepository;
        this.marcaRepository = marcaRepository;
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository = parametroRepository;
        this.cuponDescuentoRepository = cuponDescuentoRepository;
        this.imagenesPorArticuloRepository = imagenesPorArticuloRepository;
    }

    @Override
    public List<ArticuloDTOGet> findAll(String username) throws Exception {
        Cliente cliente = null;
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
            else{
                throw new NoSuchElementException("No se encuentra un usuario con username: " + username);
            }
        }
        //Creo la lista que contendra los articulos a devolver
        //List<ArticuloDTOGet> articulosAMostrar = new ArrayList<>(); AFUERRAAAAAAAAAAAAA
        //Recupero de la base de datos todos los articulos que tienen stock > 0
        List<Articulo> articulos = articuloRepository.findArticulosByCantidad();
        //Busco en la base de datos si existe alguna cotizacion del dolar guardada
        Optional<Cotizacion> cotizacion = cotizacionService.findByMoneda("USD");
        //Si existe la cotizacion recorro todos los articulos y paso el precio de dolares a pesos
        Cotizacion cotizacionDolar = null;
        if(!cotizacion.isPresent()){
             cotizacionDolar = cotizacionService.obtenerCotizacion("USD");
        }
        else{
             cotizacionDolar = cotizacion.get();
        }
        return procesarArticulos(articulos, cliente, cotizacionDolar, null);
    }

    @Override
    public Optional<ArticuloDTOGet> findById(Long id, String username) throws Exception {
        Cliente cliente = null;
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
        }
        //Recupero el articulo de la base de datos de acuerdo con el id
        Optional<Articulo> articulo = articuloRepository.findById(id);
        //Si existe busco en la base de datos la cotizacion
        if(articulo.isPresent()){
        List<Articulo> articulos = List.of(articulo.get());
        //Busco en la base de datos si existe alguna cotizacion del dolar guardada
        Optional<Cotizacion> cotizacion = cotizacionService.findByMoneda("USD");
        //Si existe la cotizacion recorro todos los articulos y paso el precio de dolares a pesos
        Cotizacion cotizacionDolar = null;
        if(!cotizacion.isPresent()){
             cotizacionDolar = cotizacionService.obtenerCotizacion("USD");
        }
        else{
             cotizacionDolar = cotizacion.get();
        }
        return Optional.of(procesarArticulos(articulos, cliente, cotizacionDolar, null).getFirst());
        }
        else{
            throw new NoSuchElementException("No se encuentra un articulo con el id: " + id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Articulo> save(List<ArticuloDTOPost> articulos) throws NoSuchElementException, NotFinalPriceException, IOException {
        //Creo la lista de articulos que se van a guardar
        List<Articulo> articulosAGuardar = new ArrayList<>();
        List<ImagenesPorArticulo> imagenes = new ArrayList<>();
        //Recorro todos los articulos que vienen en el json
        for (ArticuloDTOPost articuloDTOPost : articulos) {
            if(articuloDTOPost.PrecioFinal() == 0){
                continue;
            }
            //Recupero el articulo desde la base de datos
            Optional<Articulo> articuloBd = articuloRepository.findById(articuloDTOPost.id());
            //Si el articulo existe en base datos actualiza todos los datos con los que llegan en el nuevo json
            if(articuloBd.isPresent()){
                Articulo articuloAGuardar = articuloBd.get();
                if(articuloDTOPost.activo() == 1){
                    articuloAGuardar.setActivo(true);
                }
                else{
                    articuloAGuardar.setActivo(false);
                }
                articuloAGuardar.setCodigoDeBarra(articuloDTOPost.CodDeBarra());
                articuloAGuardar.setCodigoOrigen(articuloDTOPost.CodOrigen());
                articuloAGuardar.setDescripcion(articuloDTOPost.descripcion());
                if(articuloDTOPost.es_nuevo() == 1){
                    articuloAGuardar.setEsNuevo(true);
                }
                else{
                    articuloAGuardar.setEsNuevo(false);
                }
                if(articuloDTOPost.es_oferta() == 1){
                    articuloAGuardar.setEsOferta(true);
                }
                else{
                    articuloAGuardar.setEsOferta(false);
                }
                articuloAGuardar.setFechaDesdeOferta(articuloDTOPost.desde_fecha_oferta());
                articuloAGuardar.setFechaHastaOferta(articuloDTOPost.hasta_fecha_oferta());
                articuloAGuardar.setId(articuloDTOPost.id());
                articuloAGuardar.setNombre(articuloDTOPost.nombre());
                articuloAGuardar.setPorcentajeOferta(articuloDTOPost.porc_oferta());
                articuloAGuardar.setPreVtaMay1(articuloDTOPost.precio_m_1());
                articuloAGuardar.setPreVtaMay2(articuloDTOPost.precio_m_2());
                articuloAGuardar.setPreVtaMay3(articuloDTOPost.precio_m_3());
                articuloAGuardar.setPreVtaPub1(articuloDTOPost.precio_1());
                articuloAGuardar.setPreVtaPub2(articuloDTOPost.precio_2());
                articuloAGuardar.setPreVtaPub3(articuloDTOPost.precio_3());
                if(articuloDTOPost.precio_dolar() == 1){
                    articuloAGuardar.setPrecioDolar(true);
                }
                else{
                    articuloAGuardar.setPrecioDolar(false);
                }
                articuloAGuardar.setPrecioOferta(articuloDTOPost.precio_oferta());
                
                //Recupero el rubro de la base de datos
                Optional<Rubro> rubroBd = rubroRepository.findById(articuloDTOPost.rubros_id());
                //Si el rubro existe lo seteo
                if(rubroBd.isPresent()){
                    articuloAGuardar.setRubro(rubroBd.get());
                }
                //Su el rubro no existe verifico que exista un rubro llamado otros
                else{
                    //Verifico si existe el rubro "OTROS"
                    Optional<Rubro> sinRubro = rubroRepository.findByDescripcion("OTROS");
                    //Si el rubro existe lo seteo
                    if(sinRubro.isPresent()){
                        articuloAGuardar.setRubro(sinRubro.get());
                    }
                    //Si no existe creo un rubro llamado otros
                    else{
                        Rubro sinRubroGuardado = new Rubro();
                        sinRubroGuardado.setCodigo(Long.parseLong("000001"));
                        sinRubroGuardado.setDescripcion("OTROS");
                        sinRubroGuardado = rubroRepository.save(sinRubroGuardado);
                        articuloAGuardar.setRubro(sinRubroGuardado);
                    }
                }
                
                articuloAGuardar.setStockActual(articuloDTOPost.cantidad());
                
                //Verifico que exista el subrubro
                Optional<SubRubro> subRubro = subRubroRepository.findById(articuloDTOPost.sub_rubros_id());
                //Si el subrubro existe lo seteo
                if(subRubro.isPresent()){
                    articuloAGuardar.setSubRubro(subRubro.get());
                }
                //Si el subrubro no existe verifico que exista "OTROS"
                else{
                    //Verifico si existe el subrubro "OTROS"
                    Optional<SubRubro> sinSubRubro = subRubroRepository.findByDescripcion("OTROS");
                    //Si existe lo seteo
                    if(sinSubRubro.isPresent()){
                        articuloAGuardar.setSubRubro(sinSubRubro.get());
                    }
                    //Si no existe lo creo
                    else{
                        SubRubro sinSubRubroGuardado = new SubRubro();
                        sinSubRubroGuardado.setCodigo(Long.parseLong("000000"));
                        sinSubRubroGuardado.setDescripcion("OTROS");
                        sinSubRubroGuardado = subRubroRepository.save(sinSubRubroGuardado);
                        articuloAGuardar.setSubRubro(sinSubRubroGuardado);
                    }
                }
                //Verifico si existe la marca
                Optional<Marca> marca = marcaRepository.findById(articuloDTOPost.marca());
                //Si existe la seteo
                if(marca.isPresent()){
                    articuloAGuardar.setMarca(marca.get());
                }
                else{
                //Si no existe verifico que exista la marca "GENERAL"
                    Optional<Marca> sinMarca = marcaRepository.findByDescripcion("GENERAL");
                    //SI existe seteo
                    if(sinMarca.isPresent()){
                        articuloAGuardar.setMarca(sinMarca.get());
                    }
                    //Si no existe la creo
                    else{
                        Marca sinMarcaGuardado = new Marca();
                        sinMarcaGuardado.setCodigo(Long.parseLong("1"));
                        sinMarcaGuardado.setDescripcion("GENERAL");
                        sinMarcaGuardado = marcaRepository.save(sinMarcaGuardado);
                        articuloAGuardar.setMarca(sinMarcaGuardado);
                    }
                }
                articuloAGuardar.setAlto(articuloDTOPost.Medidas_alto());
                articuloAGuardar.setAncho(articuloDTOPost.Medidas_ancho());
                articuloAGuardar.setLargo(articuloDTOPost.Medidas_largo());
                articuloAGuardar.setPeso(articuloDTOPost.peso());
                articuloAGuardar = articuloRepository.save(articuloAGuardar);
                articulosAGuardar.add(articuloAGuardar);
            }
            else{
                //Si el articulo no existe en la base de datos, lo inicializo vacio y seteo todos los atributos
                Articulo articuloAGuardar = new Articulo();
                if(articuloDTOPost.activo() == 1){
                    articuloAGuardar.setActivo(true);
                }
                else{
                    articuloAGuardar.setActivo(false);
                }
                articuloAGuardar.setCodigoDeBarra(articuloDTOPost.CodDeBarra());
                articuloAGuardar.setCodigoOrigen(articuloDTOPost.CodOrigen());
                articuloAGuardar.setDescripcion(articuloDTOPost.descripcion());
                if(articuloDTOPost.es_nuevo() == 1){
                    articuloAGuardar.setEsNuevo(true);
                }
                else{
                    articuloAGuardar.setEsNuevo(false);
                }
                if(articuloDTOPost.es_oferta() == 1){
                    articuloAGuardar.setEsOferta(true);
                }
                else{
                    articuloAGuardar.setEsOferta(false);
                }
                articuloAGuardar.setFechaDesdeOferta(articuloDTOPost.desde_fecha_oferta());
                articuloAGuardar.setFechaHastaOferta(articuloDTOPost.hasta_fecha_oferta());
                articuloAGuardar.setId(articuloDTOPost.id());
                articuloAGuardar.setNombre(articuloDTOPost.nombre());
                articuloAGuardar.setPorcentajeOferta(articuloDTOPost.porc_oferta());
                articuloAGuardar.setPreVtaMay1(articuloDTOPost.precio_m_1());
                articuloAGuardar.setPreVtaMay2(articuloDTOPost.precio_m_2());
                articuloAGuardar.setPreVtaMay3(articuloDTOPost.precio_3());
                articuloAGuardar.setPreVtaPub1(articuloDTOPost.precio_1());
                articuloAGuardar.setPreVtaPub2(articuloDTOPost.precio_2());
                articuloAGuardar.setPreVtaPub3(articuloDTOPost.precio_3());
                if(articuloDTOPost.precio_dolar() == 1){
                    articuloAGuardar.setPrecioDolar(true);
                }
                else{
                    articuloAGuardar.setPrecioDolar(false);
                }
                articuloAGuardar.setPrecioOferta(articuloDTOPost.precio_oferta());
            
                Optional<Rubro> rubroBd = rubroRepository.findById(articuloDTOPost.rubros_id());
                if(rubroBd.isPresent()){
                    articuloAGuardar.setRubro(rubroBd.get());
                }
                else{
                    Optional<Rubro> sinRubro = rubroRepository.findByDescripcion("OTROS");
                    if(sinRubro.isPresent()){
                        articuloAGuardar.setRubro(sinRubro.get());
                    }
                    else{
                        Rubro sinRubroGuardado = new Rubro();
                        sinRubroGuardado.setCodigo(Long.parseLong("000000"));
                        sinRubroGuardado.setDescripcion("OTROS");
                        sinRubroGuardado = rubroRepository.save(sinRubroGuardado);
                        articuloAGuardar.setRubro(sinRubroGuardado);
                    }
                }
                articuloAGuardar.setStockActual(articuloDTOPost.cantidad());
            
                Optional<SubRubro> subRubro = subRubroRepository.findById(articuloDTOPost.sub_rubros_id());
                if(subRubro.isPresent()){
                    articuloAGuardar.setSubRubro(subRubro.get());
                }
                else{
                    Optional<SubRubro> sinSubRubro = subRubroRepository.findByDescripcion("OTROS");
                    if(sinSubRubro.isPresent()){
                        articuloAGuardar.setSubRubro(sinSubRubro.get());
                    }
                    else{
                        SubRubro sinSubRubroGuardado = new SubRubro();
                        sinSubRubroGuardado.setCodigo(Long.parseLong("000000"));
                        sinSubRubroGuardado.setDescripcion("OTROS");
                        sinSubRubroGuardado = subRubroRepository.save(sinSubRubroGuardado);
                        articuloAGuardar.setSubRubro(sinSubRubroGuardado);
                    }
                }

                Optional<Marca> marca = marcaRepository.findById(articuloDTOPost.marca());
                if(marca.isPresent()){
                    articuloAGuardar.setMarca(marca.get());
                }
                else{
                    Optional<Marca> sinMarca = marcaRepository.findByDescripcion("GENERAL");
                    if(sinMarca.isPresent()){
                        articuloAGuardar.setMarca(sinMarca.get());
                    }
                    else{
                        Marca sinMarcaGuardado = new Marca();
                        sinMarcaGuardado.setCodigo(Long.parseLong("1"));
                        sinMarcaGuardado.setDescripcion("GENERAL");
                        sinMarcaGuardado = marcaRepository.save(sinMarcaGuardado);
                        articuloAGuardar.setMarca(sinMarcaGuardado);
                    }
                }

                List<String> fotos = new ArrayList<>();
                articuloAGuardar.setUrlImagenes(fotos);
                articuloAGuardar.setAlto(articuloDTOPost.Medidas_alto());
                articuloAGuardar.setAncho(articuloDTOPost.Medidas_ancho());
                articuloAGuardar.setLargo(articuloDTOPost.Medidas_largo());
                articuloAGuardar.setPeso(articuloDTOPost.peso());
                articuloAGuardar = articuloRepository.save(articuloAGuardar);
                if(articuloDTOPost.fotos() != null  && !articuloDTOPost.fotos().isEmpty()){
                    ImagenesPorArticulo imagenSistema = new ImagenesPorArticulo();
                    imagenSistema.setArticulo(articuloAGuardar);
                    String path = articuloDTOPost.fotos();
                    String fileName = new File(path).getName();
                    imagenSistema.setNombreImagen(fileName);
                    imagenesPorArticuloRepository.save(imagenSistema);
                }
                articulosAGuardar.add(articuloAGuardar);
            }     
        }
        ImageStorageUtil.saveArticulosJson(articulos, "logs/articulos");
        return articulosAGuardar;
    }

    @Override
    public void delete(Long id) {
        articuloRepository.deleteById(id);
    }

    @Override
    public List<ArticuloDTOGet> actualizarPrecioArticulos(ArticuloDTOActualizacion articulos, String username) throws Exception {
        Cliente cliente = null;
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
            else{
                throw new NoSuchElementException("No se encuentra un usuario con username: " + username);
            }
        }
        //LA LOGICA DEL CUPON :0
        Double porcentajeDescuento = 0.0;
        BigDecimal maximoDescuento = BigDecimal.valueOf(0);
        //Inicicalizo la lista de precios actualizados
        List<ArticuloDTOGet> articulosActualizados = new ArrayList<>();
        List<Articulo> articulosBd = new ArrayList<>();
        for (Long articuloNuevo : articulos.articulos()) { 
            //Verifico si existe el articulo en base de datos
            Optional<Articulo> articulo = articuloRepository.findById(articuloNuevo);
            //Si el articulo existe lo agrego a la lista
            if(articulo.isPresent()){
                articulosBd.add(articulo.get());
            }
            //Si no existe aviso que el articulo no existe en bd
            else{
                throw new NoSuchElementException("No se encontro el articulo con el id: " + articulo);
            }
        }
        Optional<Cotizacion> cotizacion = cotizacionService.findByMoneda("USD");
        //Si existe la cotizacion recorro todos los articulos y paso el precio de dolares a pesos
        Cotizacion cotizacionDolar = null;
        if(!cotizacion.isPresent()){
             cotizacionDolar = cotizacionService.obtenerCotizacion("USD");
        }
        else{
             cotizacionDolar = cotizacion.get();
        }
        CuponDescuento cupon = null;
        if(articulos.codigoDescuento() != null && !articulos.codigoDescuento().equals("")){
            Optional<CuponDescuento> cuponbd = cuponDescuentoRepository.findByCodigo(articulos.codigoDescuento());
            if(cuponbd.isPresent()){
                cupon = cuponbd.get();
            }
        }
        return procesarArticulos(articulosBd, cliente, cotizacionDolar, cupon);
    }

    @Override
    public List<ArticuloDTOGet> incorporarImagenes(List<ArticuloConImagenes> imagenes) throws NoSuchElementException, IOException {
        for (ArticuloConImagenes articuloDTOCargaImagenes : imagenes) {
            Optional<Articulo> articuloBd = articuloRepository.findById(articuloDTOCargaImagenes.getArticuloId());
            if(articuloBd.isPresent()){
                Articulo articulo = articuloBd.get();
                if(articuloDTOCargaImagenes.getImagenes() != null){
                    List<MultipartFile> files = new ArrayList<>();
                    for (MultipartFile imagenNuevaArticulo : articuloDTOCargaImagenes.getImagenes()) {
                        files.add(imagenNuevaArticulo);
                    }
                    List<String> rutas = ImageStorageUtil.saveImages(files, "imagenes/articulos/articulo_" + articulo.getId());
                    if(!rutas.isEmpty()){
                        for (String rutaNueva : rutas) {
                            boolean existe = false;
                            for (String rutaExistente : articulo.getUrlImagenes()) {
                                if(rutaNueva.equals(rutaExistente)){
                                    existe = true;
                                    break;
                                }
                            }
                            if(!existe){
                                articulo.getUrlImagenes().add(rutaNueva);
                            }
                        }
                    articulo.getUrlImagenes().sort(Comparator.comparing(ruta -> {
                    String fileName = Paths.get(ruta).getFileName().toString();
                    int punto = fileName.lastIndexOf('.');
                    return (punto != -1) ? fileName.substring(0, punto) : fileName;
                    }));    
                    }
                    articulo = articuloRepository.save(articulo);
                }
            }
            else{
                continue;
            }
        }
        List<Articulo> articulosBd = articuloRepository.findAll();
        List<ArticuloDTOGet> articulosAMostrar = new ArrayList<>();
        for (Articulo articulo : articulosBd) {
            ArticuloDTOGet articuloAMostrar = new ArticuloDTOGet(articulo.getId(), articulo.getNombre(), articulo.getDescripcion(), articulo.getRubro(), articulo.getSubRubro(), articulo.getStockActual(), articulo.getPrecioOferta(), articulo.getPreVtaPub1(), articulo.isEsOferta(), articulo.isPrecioDolar(), articulo.getFechaDesdeOferta(), articulo.getFechaHastaOferta(), articulo.isEsNuevo(), articulo.isActivo(), articulo.getPorcentajeOferta(), articulo.getCodigoOrigen(), articulo.getCodigoDeBarra(), articulo.getUrlImagenes(), articulo.getAlto(), articulo.getAncho(), articulo.getLargo(), articulo.getPeso(), articulo.getAlicuotaIva(), articulo.getMarca(), articulo.isConsultar(), articulo.isDestacado());
            articulosAMostrar.add(articuloAMostrar);
        }
        return articulosAMostrar;

    }

    @Override
    public Optional<ArticuloDTOGet> findByNombre(String nombre, String username) throws Exception {
        Cliente cliente = null;
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
        }
        //Recupero el articulo de la base de datos de acuerdo con el id
        Optional<Articulo> articulo = articuloRepository.findArticulosByNombre(nombre);
        //Si existe busco en la base de datos la cotizacion
        if(articulo.isPresent()){
            List<Articulo> articulos = List.of(articulo.get());
        //Busco en la base de datos si existe alguna cotizacion del dolar guardada
        Optional<Cotizacion> cotizacion = cotizacionService.findByMoneda("USD");
        //Si existe la cotizacion recorro todos los articulos y paso el precio de dolares a pesos
        Cotizacion cotizacionDolar = null;
        if(!cotizacion.isPresent()){
             cotizacionDolar = cotizacionService.obtenerCotizacion("USD");
        }
        else{
             cotizacionDolar = cotizacion.get();
        }
        return Optional.of(procesarArticulos(articulos, cliente, cotizacionDolar, null).getFirst());
        }
        else{
            throw new NoSuchElementException("No se encuentra un articulo con el nombre: " + nombre);
        }
    }

    @Override
    public ArticuloConImagenesYErrores procesarImagenes(List<MultipartFile> imagenes) {
        List<ArticuloConImagenes> imagenesAGuardar = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        for (MultipartFile file : imagenes) {
            String fileName = file.getOriginalFilename();
            if(fileName != null){
                Long idArticulo = Long.valueOf(0);
                Optional<ImagenesPorArticulo> imagenSistema = imagenesPorArticuloRepository.findByDescripcion(fileName);
                if(imagenSistema.isPresent()){
                    idArticulo = imagenSistema.get().getArticulo().getId();
                    imagenesPorArticuloRepository.deleteById(imagenSistema.get().getId());
                }
                else{
                    Pattern pattern = Pattern.compile("^(\\d+)(\\(\\d+\\))?\\.[a-zA-Z0-9]+$");
                    Matcher matcher = pattern.matcher(fileName);
                    if(matcher.find()){
                        idArticulo = Long.parseLong(matcher.group(1));
                    }
                    else{
                        errores.add(fileName);
                        continue;
                    }
                }
                Optional<Articulo> articuloBd = articuloRepository.findById(idArticulo);
                if(articuloBd.isPresent()){
                    ArticuloConImagenes producto = buscarProducto(imagenesAGuardar, idArticulo);

                    if(producto != null){
                        producto.getImagenes().add(file);
                    }
                    else{
                        ArticuloConImagenes nuevoArticulo = new ArticuloConImagenes();
                        nuevoArticulo.setImagenes(new ArrayList<>());
                        nuevoArticulo.setArticuloId(idArticulo);
                        nuevoArticulo.getImagenes().add(file);
                        imagenesAGuardar.add(nuevoArticulo);
                    }
                }
                else{
                    errores.add(idArticulo.toString());
                }
            }
            
        }
        return new ArticuloConImagenesYErrores(imagenesAGuardar, errores);
    }

    private ArticuloConImagenes buscarProducto(List<ArticuloConImagenes> articulos, Long idArticulo){
        for (ArticuloConImagenes articuloConImagenes : articulos) {
            if(articuloConImagenes.getArticuloId().equals(idArticulo)){
                return articuloConImagenes;
            }
        }
        return null;
    }

    @Override
    public List<String> borrarImagenes(List<String> imagenes) throws IOException {
        List<String> imagenesEliminadas = new ArrayList<>();
        
        if(!imagenes.isEmpty()){
            for (String nombre : imagenes) {
                Optional<String> rutaImagen = articuloRepository.buscarRutaPorNombreImagen(nombre);
                if(rutaImagen.isPresent()){
                    imagenesEliminadas.add(rutaImagen.get());
                    List<String> imagenesBorradas = ImageStorageUtil.deleteImage(imagenesEliminadas);
                    
                    String idArticulo = nombre.replaceFirst("^([0-9]+).*", "$1");
                    Optional<Articulo> articulo = articuloRepository.findById(Long.parseLong(idArticulo));
                    if(articulo.isPresent()){
                        Articulo articuloBd = articulo.get();
                        articuloBd.getUrlImagenes().removeIf(url -> url.equals(imagenesBorradas.getFirst()));
                        articuloRepository.save(articuloBd);
                    }
                }
            }
        }
        return imagenesEliminadas;
    }

    //metodiño pa obtener listas
    public List<ArticuloDTOGet> procesarArticulos(List<Articulo> articulos, 
                                             Cliente cliente,
                                             Cotizacion cotizacion , CuponDescuento cupon) {
    ListaPrecioMinorista listaMinorista = this.parametroRepository.findByDescripcion("listaPrecioMinorista").get().getListaPrecioMinorista();
            ListaPrecioMayorista listaMayorista = this.parametroRepository.findByDescripcion("listaPrecioMayorista").get().getListaPrecioMayorista();
    return articulos.stream().map(articulo -> {
        try {
            
            // Dolariño converter   
            articulo.calcularPrecios(cliente, cotizacion.getPrecioVenta(), listaMayorista,listaMinorista);
            
            // Precio Final final
            //AAA RESULTA QUE NO LO MANDO SI ES OFERTA PORQUE SOY TORONTO Y PEDI ESO la idea era que mandaba o pre oferta o pre final pero ta mal quedo colgado esto
            BigDecimal precioFinal = articulo.obtenerPrecioFinal(cliente, listaMayorista,listaMinorista);
            if(cupon != null && !articulo.isEsOferta()){
                if((LocalDate.now().isEqual(cupon.getFechaDesde()) || LocalDate.now().isAfter(cupon.getFechaDesde())) && (LocalDate.now().isEqual(cupon.getFechaHasta()) || LocalDate.now().isBefore(cupon.getFechaHasta())) && cupon.getCantidad() >= 0 &&!(cliente != null && cliente.getTipoCliente().name().equals("MAYORISTA"))){
                    Double porcentajeDescuento = cupon.getPorcentajeDescuento();
                    BigDecimal maximoDescuento = cupon.getMontoMaximoDeDescuento();
                            BigDecimal precioActual = precioFinal;

                            BigDecimal descuento = calcularDescuento(precioActual, new BigDecimal(String.valueOf(porcentajeDescuento)), maximoDescuento);

                            articulo.setPrecioOferta(precioFinal.subtract(descuento));

                }
                else{
                    articulo.setPrecioOferta(precioFinal);
                }
            }
            return new ArticuloDTOGet(
                articulo.getId(),
                articulo.getNombre(),
                articulo.getDescripcion(),
                articulo.getRubro(),
                articulo.getSubRubro(),
                articulo.getStockActual(),
                articulo.getPrecioOferta(),
                precioFinal,
                articulo.isEsOferta(),
                articulo.isPrecioDolar(),
                articulo.getFechaDesdeOferta(),
                articulo.getFechaHastaOferta(),
                articulo.isEsNuevo(),
                articulo.isActivo(),
                articulo.getPorcentajeOferta(),
                articulo.getCodigoOrigen(),
                articulo.getCodigoDeBarra(),
                articulo.getUrlImagenes(),
                articulo.getAlto(),
                articulo.getAncho(),
                articulo.getLargo(),
                articulo.getPeso(),
                articulo.getAlicuotaIva(),
                articulo.getMarca(),
                articulo.isConsultar(),
                articulo.isDestacado()
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Error procesando artículo ID: " + articulo.getId(), e);
        }
    }).collect(Collectors.toList());
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
    public ArticuloDTOGet setConsultar(Long id, boolean consultar) throws NoSuchElementException, Exception {
        Optional<Articulo> articuloOpt = articuloRepository.findById(id);
        if (articuloOpt.isPresent()) {
            Articulo articulo = articuloOpt.get();
            articulo.setConsultar(consultar);
            articuloRepository.save(articulo);
            return new ArticuloDTOGet(articulo.getId(), articulo.getNombre(), articulo.getDescripcion(), articulo.getRubro(), articulo.getSubRubro(), articulo.getStockActual(), articulo.getPrecioOferta(), articulo.getPreVtaMay1(), articulo.isEsOferta(), articulo.isPrecioDolar(), articulo.getFechaDesdeOferta(), articulo.getFechaHastaOferta(), articulo.isEsNuevo(), articulo.isActivo(), articulo.getPorcentajeOferta(), articulo.getCodigoOrigen(), articulo.getCodigoDeBarra(), articulo.getUrlImagenes(), articulo.getAlto(), articulo.getAncho(), articulo.getLargo(), articulo.getPeso(), articulo.getAlicuotaIva(), articulo.getMarca(), articulo.isConsultar(), articulo.isDestacado());
        } else {
            throw new NoSuchElementException("No se encontró el artículo con ID: " + id);
        }
    }

    @Override
    public ArticuloDTOGet setDestacado(Long id, IsDestacado destacado) {
        Optional<Articulo> articuloBd = articuloRepository.findById(id);
        Articulo articulo = articuloBd.get();
        if(articuloBd.isPresent()){
            articulo.setDestacado(destacado.isDestacado());
        }
        articuloRepository.save(articulo);
        return new ArticuloDTOGet(articulo.getId(), articulo.getNombre(), articulo.getDescripcion(), articulo.getRubro(), articulo.getSubRubro(), articulo.getStockActual(), articulo.getPrecioOferta(), articulo.getPreVtaMay1(), articulo.isEsOferta(), articulo.isPrecioDolar(), articulo.getFechaDesdeOferta(), articulo.getFechaHastaOferta(), articulo.isEsNuevo(), articulo.isActivo(), articulo.getPorcentajeOferta(), articulo.getCodigoOrigen(), articulo.getCodigoDeBarra(), articulo.getUrlImagenes(), articulo.getAlto(), articulo.getAncho(), articulo.getLargo(), articulo.getPeso(), articulo.getAlicuotaIva(), articulo.getMarca(), articulo.isConsultar(), articulo.isDestacado());
    }

    @Override
    public List<ArticuloDTOGet> findAriticulosSinStock(String username) throws NoSuchElementException, QuotationNotFoundException, IOException {
        Cliente cliente = null;
        if(username != null){
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            if(usuario.isPresent()){
                cliente = usuario.get().getCliente();
            }
            else{
                throw new NoSuchElementException("No se encuentra un usuario con username: " + username);
            }
        }
        //Creo la lista que contendra los articulos a devolver
        //List<ArticuloDTOGet> articulosAMostrar = new ArrayList<>(); AFUERRAAAAAAAAAAAAA
        //Recupero de la base de datos todos los articulos que tienen stock > 0
        List<Articulo> articulos = articuloRepository.findArticulosSinStock();
        //Busco en la base de datos si existe alguna cotizacion del dolar guardada
        Optional<Cotizacion> cotizacion = cotizacionService.findByMoneda("USD");
        //Si existe la cotizacion recorro todos los articulos y paso el precio de dolares a pesos
        Cotizacion cotizacionDolar = null;
        if(!cotizacion.isPresent()){
             cotizacionDolar = cotizacionService.obtenerCotizacion("USD");
        }
        else{
             cotizacionDolar = cotizacion.get();
        }
        return procesarArticulos(articulos, cliente, cotizacionDolar, null);
    }

    @Override
    @Async
    public void procesarImagenesAsync(List<MultipartFile> imagenes) throws NoSuchElementException, IOException {
        ArticuloConImagenesYErrores imagenesAGuardar = procesarImagenes(imagenes);
        incorporarImagenes(imagenesAGuardar.imagenes());
    }
}
