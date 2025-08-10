package easycommerce.easycommerce.Autenticacion.filtros;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import easycommerce.easycommerce.Autenticacion.SimpleGrantedAuthorityJsonCreator;
import easycommerce.easycommerce.Autenticacion.TokenJWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter{

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null || header.equals("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        
        try {
            Claims claims = Jwts.parser().verifyWith(TokenJWTConfig.SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            Object authoritiesClaims = claims.get("authorities");
            String username = claims.getSubject();
            
            List<GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken authorization = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authorization);
            chain.doFilter(request, response);
            return;
        } catch (JwtException e) {
            Map<String,String> body = new HashMap<>();
            body.put("messagge", "Token jwt no es valido");

            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            return;
        }
    }   
}
