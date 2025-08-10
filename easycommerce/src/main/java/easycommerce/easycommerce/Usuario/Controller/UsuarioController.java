package easycommerce.easycommerce.Usuario.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Correo.CorreoService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.UserAlreadyVerifiedException;
import easycommerce.easycommerce.Usuario.DTOs.DTOPostUsuario;
import easycommerce.easycommerce.Usuario.DTOs.DTOPutUsuario;
import easycommerce.easycommerce.Usuario.DTOs.UsuarioDTOGet;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Service.UsuarioService;
import jakarta.mail.MessagingException;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CorreoService correoService;

    public UsuarioController(UsuarioService usuarioService, CorreoService correoService) {
        this.usuarioService = usuarioService;
        this.correoService = correoService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTOGet>> getAllUsuarios(){
        List<UsuarioDTOGet> usuarios = usuarioService.findAll();
        return new ResponseEntity<>(usuarios,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOGet> getUsuarioPorId(@PathVariable Long id) throws NoSuchElementException{
        Optional<UsuarioDTOGet> usuario = usuarioService.findByIdMostrar(id);
        if(usuario.isPresent()){
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentra el usuario con id: " + id);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> getUsuarioPorUsername(@PathVariable String username) throws NoSuchElementException{
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        if(usuario.isPresent()){
            return new ResponseEntity<>(usuario.get(),HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentra el usuario con el usuername: " + username);
        }
    }

    @GetMapping("/usuarioLogueado")
    public ResponseEntity<Map<String,Object>> getUsuarioLogueado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        Optional<Usuario> usuarioLogueado = usuarioService.findByUsername(username);
        if(usuarioLogueado.isPresent()){
            if(usuarioLogueado.get().isValidado()){
                Map<String, Object>  body = new HashMap<>();
                body.put("username", usuarioLogueado.get().getUsername());
                if(usuarioLogueado.get().getCliente() != null){
                    body.put("apellido", usuarioLogueado.get().getCliente().getApellido());
                    body.put("nombre", usuarioLogueado.get().getCliente().getNombre());
                    body.put("documento", usuarioLogueado.get().getCliente().getDocumento());
                    body.put("telefono", usuarioLogueado.get().getCliente().getTelefono());
                    body.put("razonSocial", usuarioLogueado.get().getCliente().getRazonSocial());
                    if(usuarioLogueado.get().getCliente().getCondicionIva() != null){
                        body.put("condicionIva", usuarioLogueado.get().getCliente().getCondicionIva().name());
                    }
                    else{
                        body.put("condicionIva", null);
                    }
                    body.put("direcciones", usuarioLogueado.get().getCliente().getDirecciones());
                    body.put("tipoCliente", usuarioLogueado.get().getCliente().getTipoCliente().name());
                    body.put("idCliente", usuarioLogueado.get().getCliente().getId());
                }
                return new ResponseEntity<>(body,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);                    
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuarioLogueado/admin")
    public ResponseEntity<Boolean> getUsuarioLogueadoAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        Optional<Usuario> usuarioLogueado = usuarioService.findByUsername(username);
        if(usuarioLogueado.isPresent()){
            if(usuarioLogueado.get().isValidado() && usuarioLogueado.get().getRol().getDescripcion().equals("ROLE_ADMINISTRADOR")){
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }                    
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Void> saveUsuario(@RequestBody DTOPostUsuario usuario) throws EntityAlreadyExistsException, NoSuchElementException, MessagingException, UserAlreadyVerifiedException{
        UsuarioDTOGet usuarioGuardado = usuarioService.save(usuario);
        correoService.enviarMailCodigoVerficacion(usuarioGuardado.username());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOGet> updateUsuario(@PathVariable Long id, @RequestBody DTOPutUsuario usuario) throws NoSuchElementException{
        Optional<Usuario> usuarioBd = usuarioService.findById(id);
        if(usuarioBd.isPresent()){
            Usuario usuarioAGuardar = usuarioBd.get();
            if(usuario.password() != null){
                usuarioAGuardar.setPassword(usuario.password());
            }
            UsuarioDTOGet usuarioGuardado = usuarioService.update(usuarioAGuardar);
            return new ResponseEntity<>(usuarioGuardado,HttpStatus.CREATED);
        }
        else{
            throw new NoSuchElementException("No se encuentra un usuario con el id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) throws NoSuchElementException{
        Optional<Usuario> usuario = usuarioService.findById(id);
        if(usuario.isPresent()){
            usuarioService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            throw new NoSuchElementException("No existe un cliente con el id: " + id);
        }
    }
}
