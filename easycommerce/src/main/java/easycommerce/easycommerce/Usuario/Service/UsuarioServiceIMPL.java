package easycommerce.easycommerce.Usuario.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cliente.Model.TipoCliente;
import easycommerce.easycommerce.Cliente.Repository.ClienteRepository;
import easycommerce.easycommerce.CodigoVerificacion.Model.CodigoVerificacion;
import easycommerce.easycommerce.CodigoVerificacion.Service.CodigoVerificacionService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Rol.Model.Rol;
import easycommerce.easycommerce.Rol.Repository.RolRepository;
import easycommerce.easycommerce.Usuario.DTOs.DTOPostUsuario;
import easycommerce.easycommerce.Usuario.DTOs.UsuarioDTOGet;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;



@Service
public class UsuarioServiceIMPL implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodigoVerificacionService codigoVerificacionService;
    private final RolRepository rolRepository;

    public UsuarioServiceIMPL(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder, CodigoVerificacionService codigoVerificacionService,
            RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.codigoVerificacionService = codigoVerificacionService;
        this.rolRepository = rolRepository;
    }

    @Override
    public List<UsuarioDTOGet> findAll() {
        return usuarioRepository.findAll()
            .stream()
            .filter(usuario -> usuario.getCliente() != null)
            .map(usuario -> new UsuarioDTOGet(usuario.getId(), usuario.getUsername(), usuario.getRol(), usuario.getCliente(), usuario.isValidado()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioDTOGet> findByIdMostrar(Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> new UsuarioDTOGet(id, usuario.getUsername(), usuario.getRol(), usuario.getCliente(), usuario.isValidado()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTOGet save(DTOPostUsuario usuario) throws EntityAlreadyExistsException, NoSuchElementException {
        
        Optional<Usuario> usuarioRegistrado = usuarioRepository.findByUsername(usuario.username());
        if(usuarioRegistrado.isPresent() && usuarioRegistrado.get().isValidado()){
            throw new EntityAlreadyExistsException("Ya existe un usuario registrado con el username: " + usuario.username());
        }
        Usuario usuarioNuevo = new Usuario();
        Optional<Cliente> clienteBd = clienteRepository.findByDocumento(usuario.documento());
        //Si el cliente existe seteo las variables del cliente guardado con las que me llegan por parametro
        if(clienteBd.isPresent()){
            Optional<Usuario> usuarioBd = usuarioRepository.findByCliente(clienteBd.get().getId());
            if(!usuarioBd.isPresent()){
                Cliente cliente = clienteBd.get();
                cliente.setDocumento(usuario.documento());
                if(cliente.getRazonSocial() != null){
                    cliente.setRazonSocial(usuario.razonSocial());
                }
                else{
                    cliente.setNombre(usuario.nombre());
                    cliente.setApellido(usuario.apellido());
                } 
                usuarioNuevo.setCliente(cliente);
            }
            else{
                if(clienteBd.get().getRazonSocial() != null && usuarioBd.get().isValidado()){
                    throw new EntityAlreadyExistsException("Ya se encuentra un usuario registrado para el cliente: " + clienteBd.get().getRazonSocial());
                }
                else if(clienteBd.get().getNombre() != null && clienteBd.get().getApellido() != null && usuarioBd.get().isValidado()){
                    throw new EntityAlreadyExistsException("Ya se encuentra un usuario registrado para el cliente: " + clienteBd.get().getNombre() + " " + clienteBd.get().getApellido());
                }
                else if(clienteBd.get().getRazonSocial() != null && !usuarioBd.get().isValidado()){
                    CodigoVerificacion codigoNuevo = new CodigoVerificacion();
                    codigoNuevo.setCodigo(codigoVerificacionService.generarCodigoVerificacion());
                    codigoNuevo.setTiempoExpiracion(LocalDateTime.now().plusHours(1));
                    codigoNuevo.setValidado(false);
                    usuarioBd.get().setCodigoVerificacion(codigoNuevo);
                    Usuario usaurioActualizado = usuarioRepository.save(usuarioBd.get());
                    return new UsuarioDTOGet(usaurioActualizado.getId(), usaurioActualizado.getUsername(), usaurioActualizado.getRol(), usaurioActualizado.getCliente(), usaurioActualizado.isValidado());
                }
                else if(clienteBd.get().getNombre() != null && clienteBd.get().getApellido() != null && !usuarioBd.get().isValidado()){
                    CodigoVerificacion codigoNuevo = new CodigoVerificacion();
                    codigoNuevo.setCodigo(codigoVerificacionService.generarCodigoVerificacion());
                    codigoNuevo.setTiempoExpiracion(LocalDateTime.now().plusHours(1));
                    codigoNuevo.setValidado(false);
                    usuarioBd.get().setCodigoVerificacion(codigoNuevo);
                    Usuario usaurioActualizado = usuarioRepository.save(usuarioBd.get());
                    return new UsuarioDTOGet(usaurioActualizado.getId(), usaurioActualizado.getUsername(), usaurioActualizado.getRol(), usaurioActualizado.getCliente(), usaurioActualizado.isValidado());
                }   
            }
        }
        //Si el cliente no existe creo un cliente nuevo seteando en sus atributos lo uqe me llega por parametro
        else{
            Cliente cliente = new Cliente();
            cliente.setDocumento(usuario.documento());
            cliente.setEmail(usuario.username());
            if(usuario.razonSocial() != ""){
                cliente.setRazonSocial(usuario.razonSocial());
            }
            else{
                cliente.setNombre(usuario.nombre());
                cliente.setApellido(usuario.apellido());
            }
            cliente.setTipoCliente(TipoCliente.MINORISTA);
            cliente.setDirecciones(new ArrayList<>());
            usuarioNuevo.setCliente(cliente);
        }
        //Encrito la contraseña para guardarla en la base de datos
        usuarioNuevo.setUsername(usuario.username());
        String passwordEncriptado = passwordEncoder.encode(usuario.password());
        usuarioNuevo.setPassword(passwordEncriptado);
        usuarioNuevo.setValidado(false);
        CodigoVerificacion codigo = new CodigoVerificacion();
        codigo.setValidado(false);
        codigo.setTiempoExpiracion(LocalDateTime.now().plusHours(1));
        codigo.setCodigo(codigoVerificacionService.generarCodigoVerificacion());
        usuarioNuevo.setCodigoVerificacion(codigo);
        if(usuario.rolId() != null){
            Optional<Rol> rol = rolRepository.findById(usuario.rolId());
            if(rol.isPresent()){
                usuarioNuevo.setRol(rol.get());
            }
            else{
                throw new NoSuchElementException("No se encontro el rol especificado");
            }
        }
        else{
            Optional<Rol> rol = rolRepository.findByDescripcion("ROLE_CLIENTE");
            if(rol.isPresent()){
                usuarioNuevo.setRol(rol.get());
            }
            else{
                throw new NoSuchElementException("No existe el rol especificado");
            }
        }
        //Guardo el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuarioNuevo);
        return new UsuarioDTOGet(usuarioGuardado.getId(), usuarioGuardado.getUsername(), usuarioGuardado.getRol(), usuarioGuardado.getCliente(), usuarioGuardado.isValidado());
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(String username){
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public UsuarioDTOGet update(Usuario usuario) {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTOGet(usuarioGuardado.getId(), usuarioGuardado.getUsername(), usuarioGuardado.getRol(), usuarioGuardado.getCliente(), usuarioGuardado.isValidado());
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    

}
