package easycommerce.easycommerce.Usuario.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Usuario.DTOs.DTOPostUsuario;
import easycommerce.easycommerce.Usuario.DTOs.UsuarioDTOGet;
import easycommerce.easycommerce.Usuario.Model.Usuario;



public interface UsuarioService {
    List<UsuarioDTOGet> findAll();
    Optional<UsuarioDTOGet> findByIdMostrar(Long id);
    UsuarioDTOGet save(DTOPostUsuario usuario) throws EntityAlreadyExistsException, NoSuchElementException;
    void delete(Long id);
    Optional<Usuario> findByUsername(String username);
    UsuarioDTOGet update(Usuario usuario);
    Optional<Usuario> findById(Long id);
}
