package easycommerce.easycommerce.CuponDescuento.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;
import easycommerce.easycommerce.CuponDescuento.DTOs.CuponDescuentoDTOGet;
import easycommerce.easycommerce.CuponDescuento.Service.CuponDescuentoService;
import easycommerce.easycommerce.Excepciones.InvalidCouponCodeException;

@RestController
@CrossOrigin
@RequestMapping("/cuponDescuento")
public class CuponDescuentoController {

    private final CuponDescuentoService cuponDescuentoService;

    public CuponDescuentoController(CuponDescuentoService cuponDescuentoService) {
        this.cuponDescuentoService = cuponDescuentoService;
    }

    @GetMapping
    public ResponseEntity<List<CuponDescuentoDTOGet>> getAllCuponesDescuento(){
        List<CuponDescuentoDTOGet> cupones = cuponDescuentoService.findAll();
        return new ResponseEntity<>(cupones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuponDescuentoDTOGet> getCuponDescuentoById(@PathVariable Long id){
        Optional<CuponDescuentoDTOGet> cuponBd = cuponDescuentoService.findByIdMostrar(id);
        if(cuponBd.isPresent()){
            return new ResponseEntity<>(cuponBd.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponDescuentoDTOGet> getCuponDescuentoByCodigo(@PathVariable String codigo){
        Optional<CuponDescuentoDTOGet> cupon = cuponDescuentoService.findByCodigoMostrar(codigo);
        if(cupon.isPresent()){
            return new ResponseEntity<>(cupon.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<CuponDescuentoDTOGet> saveCuponDescuento(@RequestBody CuponDescuento cuponDescuento){
        CuponDescuentoDTOGet cupon = cuponDescuentoService.save(cuponDescuento);
        return new ResponseEntity<>(cupon, HttpStatus.CREATED);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<CuponDescuentoDTOGet> updateCuponDescuento(@RequestBody CuponDescuento cuponDescuento, @PathVariable String codigo){
        Optional<CuponDescuento> cuponBd = cuponDescuentoService.findByCodigo(codigo);
        if(cuponBd.isPresent()){
            CuponDescuento cupon = cuponBd.get();
            if(cuponDescuento.getCantidad() != null){
                cupon.setCantidad(cuponDescuento.getCantidad());
            }
            if(cuponDescuento.getFechaDesde() != null){
                cupon.setFechaDesde(cuponDescuento.getFechaDesde());
            }
            if(cuponDescuento.getFechaHasta() != null){
                cupon.setFechaHasta(cuponDescuento.getFechaHasta());
            }
            CuponDescuentoDTOGet cuponGuardado = cuponDescuentoService.save(cuponDescuento);
            return new ResponseEntity<>(cuponGuardado, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuponDescuento(@PathVariable Long id){
        Optional<CuponDescuento> cupon = cuponDescuentoService.findById(id);
        if(cupon.isPresent()){
            cuponDescuentoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/validarCodigo/{codigo}")
    public ResponseEntity<Boolean> validarCupon(@PathVariable String codigo) throws InvalidCouponCodeException{
        boolean aplicarCupon = cuponDescuentoService.validarCodigoDescuento(codigo);
        return new ResponseEntity<>(aplicarCupon,HttpStatus.OK);
    }
}