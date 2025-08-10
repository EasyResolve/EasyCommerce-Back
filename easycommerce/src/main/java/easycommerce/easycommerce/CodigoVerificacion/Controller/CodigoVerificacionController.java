package easycommerce.easycommerce.CodigoVerificacion.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOUsuarioValidado;
import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOValidarCodigo;
import easycommerce.easycommerce.CodigoVerificacion.Model.CodigoVerificacion;
import easycommerce.easycommerce.CodigoVerificacion.Service.CodigoVerificacionService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Service.UsuarioService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;

@RestController
@CrossOrigin
@RequestMapping("/codigoVerificacion")
public class CodigoVerificacionController {

    private final CodigoVerificacionService codigoVerificacionService;
    private final UsuarioService usuarioService;


    public CodigoVerificacionController(CodigoVerificacionService codigoVerificacionService,
            UsuarioService usuarioService) {
        this.codigoVerificacionService = codigoVerificacionService;
        this.usuarioService = usuarioService;
    }


    @PostMapping("/validar")
    public ResponseEntity<DTOUsuarioValidado> validarCodigo(@RequestBody DTOValidarCodigo dtoValidarCodigo) throws Exception{
        DTOUsuarioValidado usuarioValidado = codigoVerificacionService.validarCodigo(dtoValidarCodigo.username(), dtoValidarCodigo.codigo());
        return new ResponseEntity<>(usuarioValidado, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<CodigoVerificacion> generarNuevoCodigo(@PathVariable String username) throws EntityAlreadyExistsException, NoSuchElementException{
        Optional<Usuario> usuarioBd = usuarioService.findByUsername(username);
        if(usuarioBd.isPresent()){
            Usuario usuario = usuarioBd.get();
            if(!usuario.getCodigoVerificacion().isValidado()){
                CodigoVerificacion codigoVerificacion = usuario.getCodigoVerificacion();
                codigoVerificacion.setCodigo(codigoVerificacionService.generarCodigoVerificacion());
                codigoVerificacion.setTiempoExpiracion(LocalDateTime.now().plusMinutes(1));
                codigoVerificacion.setValidado(false);
                codigoVerificacion = codigoVerificacionService.save(codigoVerificacion);
                return new ResponseEntity<>(codigoVerificacion,HttpStatus.CREATED);
            }
            else{
                throw new EntityAlreadyExistsException("El usuario ya se encuentra validado con exito");
            }
        }
        else{
            throw new NoSuchElementException("El usuario indicado no se encuentra en la base de datos");
        }
    }
    
}
