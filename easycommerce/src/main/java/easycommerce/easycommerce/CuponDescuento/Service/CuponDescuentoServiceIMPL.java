package easycommerce.easycommerce.CuponDescuento.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import easycommerce.easycommerce.CuponDescuento.DTOs.CuponDescuentoDTOGet;
import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.Excepciones.InvalidCouponCodeException;
import easycommerce.easycommerce.CuponDescuento.Repository.CuponDescuentoRepository;

@Service
public class CuponDescuentoServiceIMPL implements CuponDescuentoService {

    private final CuponDescuentoRepository cuponDescuentoRepository;

    public CuponDescuentoServiceIMPL(CuponDescuentoRepository cuponDescuentoRepository) {
        this.cuponDescuentoRepository = cuponDescuentoRepository;
    }

    @Override
    public List<CuponDescuentoDTOGet> findAll() {
        return cuponDescuentoRepository.findAll()
            .stream()
            .map(cupon -> new CuponDescuentoDTOGet(cupon.getId(), cupon.getCodigo(), cupon.getFechaDesde(), cupon.getFechaHasta(), cupon.getCantidad(), cupon.getPorcentajeDescuento(), cupon.getMontoMaximoDeDescuento()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CuponDescuentoDTOGet> findByIdMostrar(Long id) {
        return cuponDescuentoRepository.findById(id)
            .map(cupon -> new CuponDescuentoDTOGet(id, cupon.getCodigo(), cupon.getFechaDesde(), cupon.getFechaHasta(), cupon.getCantidad(), cupon.getPorcentajeDescuento(), cupon.getMontoMaximoDeDescuento()));
    }

    @Override
    public CuponDescuentoDTOGet save(CuponDescuento cuponDescuento) {
        CuponDescuento cuponGuardado = cuponDescuentoRepository.save(cuponDescuento);
        return new CuponDescuentoDTOGet(cuponGuardado.getId(), cuponGuardado.getCodigo(), cuponGuardado.getFechaDesde(),cuponGuardado.getFechaHasta() , cuponGuardado.getCantidad(), cuponGuardado.getPorcentajeDescuento(), cuponGuardado.getMontoMaximoDeDescuento());
    }

    @Override
    public void delete(Long id) {
        cuponDescuentoRepository.deleteById(id);
    }

    @Override
    public Optional<CuponDescuentoDTOGet> findByCodigoMostrar(String codigo) {
        return cuponDescuentoRepository.findByCodigo(codigo)
            .map(cupon -> new CuponDescuentoDTOGet(cupon.getId(), codigo, cupon.getFechaDesde(), cupon.getFechaHasta(), cupon.getCantidad(), cupon.getPorcentajeDescuento(), cupon.getMontoMaximoDeDescuento()));
    }

    @Override
    public boolean validarCodigoDescuento(String codigo) throws InvalidCouponCodeException {
        Optional<CuponDescuento> cuponBd = cuponDescuentoRepository.findByCodigo(codigo);
        if(cuponBd.isPresent()){
            CuponDescuento cupon = cuponBd.get();
            if((LocalDate.now().isEqual(cupon.getFechaDesde()) || LocalDate.now().isAfter(cupon.getFechaDesde())) && (LocalDate.now().isEqual(cupon.getFechaHasta()) || LocalDate.now().isBefore(cupon.getFechaHasta())) && cupon.getCantidad() >= 0){
                return true;
            }
            return false;
        }
        else{
            return false;
        }
    }

    @Override
    public Optional<CuponDescuento> findByCodigo(String codigo) {
        return cuponDescuentoRepository.findByCodigo(codigo);
    }

    @Override
    public Optional<CuponDescuento> findById(Long id) {
        return cuponDescuentoRepository.findById(id);
    }

    
}
