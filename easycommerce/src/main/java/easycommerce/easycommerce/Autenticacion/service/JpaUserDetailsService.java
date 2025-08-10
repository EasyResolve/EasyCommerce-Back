package easycommerce.easycommerce.Autenticacion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easycommerce.easycommerce.Rol.Model.Rol;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;


@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioBd = usuarioRepository.findByUsername(username);
        if(!usuarioBd.isPresent()){
            throw new UsernameNotFoundException(String.format("Username %s, no encontrado en el sistema", username));
        }
        if(!usuarioBd.get().isValidado()){
            throw new UsernameNotFoundException(String.format("El usuario con username %s, no ha sido validado con el codigo enviado", username));
        }
        List<Rol> roles = new ArrayList<>();
        Usuario usuario = usuarioBd.get();
        roles.add(usuario.getRol());
        List<GrantedAuthority> authorities = roles.stream()
            .map(r -> new SimpleGrantedAuthority(r.getDescripcion()))
            .collect(Collectors.toList());
        return new User(usuario.getUsername(), usuario.getPassword(), true, true,true,true,authorities);
    }

}
