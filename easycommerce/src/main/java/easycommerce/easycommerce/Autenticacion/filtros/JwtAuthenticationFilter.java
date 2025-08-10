package easycommerce.easycommerce.Autenticacion.filtros;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import easycommerce.easycommerce.Autenticacion.TokenJWTConfig;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Usuario usuario = null;
        String username = null;
        String password = null;

        try {
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = usuario.getUsername();
            password = usuario.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String username = ((User)authResult.getPrincipal()).getUsername();
        
        Usuario usuario = usuarioRepository.findByUsername(username).get();
        
        Collection<? extends GrantedAuthority> rol = authResult.getAuthorities();
        Map<String, Object> roles = new HashMap<>();
        roles.put("authorities", new ObjectMapper().writeValueAsString(rol));
        Claims claims = Jwts.claims().add(roles).build();


        String token = Jwts.builder()
            .claims(claims)
            .subject(username)
            .signWith(TokenJWTConfig.SECRET_KEY)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 604800017))
            .compact();

        response.addHeader(TokenJWTConfig.HEADER_AUTHORIZATION, TokenJWTConfig.PREFIX_TOKEN + token);
        
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito!", username));
        body.put("username", usuario.getUsername());
        if(usuario.getCliente() != null){
            body.put("apellido", usuario.getCliente().getApellido());
            body.put("nombre", usuario.getCliente().getNombre());
            body.put("documento", usuario.getCliente().getDocumento());
            body.put("telefono", usuario.getCliente().getTelefono());
            body.put("razonSocial", usuario.getCliente().getRazonSocial());
            if(usuario.getCliente().getCondicionIva() != null){
                body.put("condicionIva", usuario.getCliente().getCondicionIva().name());
            }
            else{
                body.put("condicionIva", null);
            }
            body.put("direcciones", usuario.getCliente().getDirecciones());
            body.put("tipoCliente", usuario.getCliente().getTipoCliente().name());
            body.put("idCliente", usuario.getCliente().getId());
        }
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error en la autenticacion username o password incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }
    
}
