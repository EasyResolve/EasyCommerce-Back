package easycommerce.easycommerce.Articulo.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOActualizacion;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOGet;
import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOPost;
import easycommerce.easycommerce.Articulo.DTOs.IsDestacado;
import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Articulo.Model.ArticuloConImagenes;
import easycommerce.easycommerce.Articulo.Model.ArticuloConImagenesYErrores;
import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;
import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.NotFinalPriceException;
import easycommerce.easycommerce.Excepciones.QuotationNotFoundException;



public interface ArticuloService {
    List<ArticuloDTOGet> findAll(String username) throws QuotationNotFoundException, IOException, NoSuchElementException, Exception;
    Optional<ArticuloDTOGet> findById(Long id, String username) throws Exception;
    List<Articulo> save(List<ArticuloDTOPost> articulos) throws NoSuchElementException, NotFinalPriceException, IOException;
    void delete(Long id);
    //List<ArticuloDTOGet> findByRubro(Long id, String username) throws NoSuchElementException, QuotationNotFoundException, IOException, Exception;
    List<ArticuloDTOGet> actualizarPrecioArticulos(ArticuloDTOActualizacion articulos, String username) throws NoSuchElementException, QuotationNotFoundException, IOException, Exception;
    List<ArticuloDTOGet> incorporarImagenes(List<ArticuloConImagenes> imagenes) throws NoSuchElementException, IOException;
    Optional<ArticuloDTOGet> findByNombre(String nombre, String username) throws NoSuchElementException, Exception;
    ArticuloConImagenesYErrores procesarImagenes(List<MultipartFile> imagenes);
    List<String> borrarImagenes(List<String> imagenes) throws IOException;
    List<ArticuloDTOGet> procesarArticulos(List<Articulo> articulos,
                                           Cliente cliente,
                                           Cotizacion cotizacion, CuponDescuento cupon);
    ArticuloDTOGet setConsultar(Long id, boolean consultar) throws NoSuchElementException, Exception;
    ArticuloDTOGet setDestacado(Long id, IsDestacado destacado);
}
