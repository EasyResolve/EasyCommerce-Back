package easycommerce.easycommerce.CodigoCambioPassword.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.CodigoCambioPassword.DTOs.DTOUsuarioCambioPassword;
import easycommerce.easycommerce.CodigoCambioPassword.Repository.CodigoCambioPasswordRepository;
import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOUsuarioValidado;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.TokenExpiredException;
import easycommerce.easycommerce.Excepciones.UserAlreadyVerifiedException;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;
import easycommerce.easycommerce.CodigoCambioPassword.Model.CodigoCambioPassword;

@Service
public class CodigoCambioPasswordService {
    private final CodigoCambioPasswordRepository cambioPasswordRepository;
    private final UsuarioRepository usuarioRepository;
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public CodigoCambioPasswordService(CodigoCambioPasswordRepository cambioPasswordRepository,
            UsuarioRepository usuarioRepository) {
        this.cambioPasswordRepository = cambioPasswordRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public String generarCodigoVerificacion(){
        return RANDOM.ints(4, 0, CARACTERES.length())
        .mapToObj(CARACTERES::charAt)
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        .toString();
    }

    public DTOUsuarioValidado validarCodigo(String username, String codigoIngresado) throws Exception{
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if(usuario.isPresent()){
            CodigoCambioPassword codigo = usuario.get().getCodigoCambioPassword();
            if(codigo.isValidado()){
                throw new UserAlreadyVerifiedException("El usuario ya se encuentra validado exitosamente");
            }
            if(codigo.estaExpirado()){
                throw new TokenExpiredException("El codigo ha expirado, por favor solicite un nuevo codigo");
            }
            if(codigo.getCodigo().equals(codigoIngresado)){
                usuario.get().setValidado(true);
                usuarioRepository.save(usuario.get());

                codigo.setValidado(true);
                cambioPasswordRepository.save(codigo);
                DTOUsuarioValidado usuarioValidado = new DTOUsuarioValidado(usuario.get().getId(), usuario.get().getUsername(),usuario.get().getRol(), usuario.get().getCliente(), true);
                return usuarioValidado;
            }
            else{
                throw new NoSuchElementException("El codigo ingresado no concuerda con el codigo enviado");
            }
        }
        else{
            throw new NoSuchElementException("El usuario indicado no se encuentra registrado en la base de datos");
        }
    }

    public boolean save(DTOUsuarioCambioPassword usuario){
        Optional<Usuario> usuarioBd = usuarioRepository.findByUsername(usuario.username());
        if(usuarioBd.isPresent()){
            CodigoCambioPassword codigo = usuarioBd.get().getCodigoCambioPassword();
            codigo.setCodigo(generarCodigoVerificacion());
            codigo.setTiempoExpiracion(LocalDateTime.now().plusMinutes(2));
            codigo.setValidado(false);
            codigo = cambioPasswordRepository.save(codigo);
            return true;
        }
        else{
            CodigoCambioPassword codigoNuevo = new CodigoCambioPassword();
            codigoNuevo.setCodigo(generarCodigoVerificacion());
            codigoNuevo.setTiempoExpiracion(LocalDateTime.now().plusMinutes(2));
            codigoNuevo.setValidado(false);
            codigoNuevo = cambioPasswordRepository.save(codigoNuevo);
            return true;
        }
    }
}
