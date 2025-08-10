package easycommerce.easycommerce.CodigoVerificacion.Service;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.CodigoVerificacion.DTOs.DTOUsuarioValidado;
import easycommerce.easycommerce.CodigoVerificacion.Model.CodigoVerificacion;
import easycommerce.easycommerce.CodigoVerificacion.Repository.CodigoVerificacionRepository;
import easycommerce.easycommerce.Correo.CorreoService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.TokenExpiredException;
import easycommerce.easycommerce.Excepciones.UserAlreadyVerifiedException;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;



@Service
public class CodigoVerificacionService {
    private final CodigoVerificacionRepository codigoVerificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CorreoService correoService;
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public CodigoVerificacionService(CodigoVerificacionRepository codigoVerificacionRepository,
            UsuarioRepository usuarioRepository, CorreoService correoService) {
        this.codigoVerificacionRepository = codigoVerificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.correoService = correoService;
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
            CodigoVerificacion codigo = usuario.get().getCodigoVerificacion();
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
                codigoVerificacionRepository.save(codigo);
                DTOUsuarioValidado usuarioValidado = new DTOUsuarioValidado(usuario.get().getId(), usuario.get().getUsername(),usuario.get().getRol(), usuario.get().getCliente(), true);

                correoService.enviarMailRegistroCorrecto(usuario.get().getUsername());
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

    public CodigoVerificacion save(CodigoVerificacion codigoVerificacion){
        return codigoVerificacionRepository.save(codigoVerificacion);
    }
}
