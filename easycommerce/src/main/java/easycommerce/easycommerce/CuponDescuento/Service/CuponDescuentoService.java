package easycommerce.easycommerce.CuponDescuento.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.CuponDescuento.DTOs.CuponDescuentoDTOGet;
import easycommerce.easycommerce.Excepciones.InvalidCouponCodeException;

public interface CuponDescuentoService {
    List<CuponDescuentoDTOGet> findAll();
    Optional<CuponDescuentoDTOGet> findByIdMostrar(Long id);
    CuponDescuentoDTOGet save(CuponDescuento cuponDescuento);
    void delete(Long id);
    Optional<CuponDescuentoDTOGet> findByCodigoMostrar(String codigo);
    boolean validarCodigoDescuento(String codigo) throws InvalidCouponCodeException;
    Optional<CuponDescuento> findByCodigo(String codigo);
    Optional<CuponDescuento> findById(Long id);
}
