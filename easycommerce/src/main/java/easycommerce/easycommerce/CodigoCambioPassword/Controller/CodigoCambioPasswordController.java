package easycommerce.easycommerce.CodigoCambioPassword.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.CodigoCambioPassword.DTOs.DTOUsuarioCambioPassword;
import easycommerce.easycommerce.CodigoCambioPassword.Service.CodigoCambioPasswordService;
import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOUsuarioValidado;
import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOValidarCodigo;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/codigoCambioPassword")
public class CodigoCambioPasswordController {

    private final CodigoCambioPasswordService codigoCambioPasswordService;

    public CodigoCambioPasswordController(CodigoCambioPasswordService codigoCambioPasswordService) {
        this.codigoCambioPasswordService = codigoCambioPasswordService;
    }

    @PostMapping("/crearCodigo")
    public ResponseEntity<Boolean> generarCodigoCambioPassword(@RequestBody DTOUsuarioCambioPassword usuario){
        boolean creado = codigoCambioPasswordService.save(usuario);
        return new ResponseEntity<>(creado,HttpStatus.CREATED);
    }
    
    @PostMapping("/ValidarCodigo")
    public ResponseEntity<DTOUsuarioValidado> validarCambioContraseña(DTOValidarCodigo dtoValidarCodigo) throws Exception{
        DTOUsuarioValidado cambioValidado = codigoCambioPasswordService.validarCodigo(dtoValidarCodigo.username(), dtoValidarCodigo.codigo());
        return new ResponseEntity<>(cambioValidado,HttpStatus.OK);
    } 
}
