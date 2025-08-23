package easycommerce.easycommerce.Autenticacion;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import easycommerce.easycommerce.Autenticacion.filtros.JwtAuthenticationFilter;
import easycommerce.easycommerce.Autenticacion.filtros.JwtValidationFilter;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UsuarioRepository usuarioRepository;

    
    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, UsuarioRepository usuarioRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Order(1)
    SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/nave/notificacion") // solo aplica a este endpoint
        .authorizeHttpRequests(authz -> authz
            .anyRequest().hasRole("NAVE")
        )
        .csrf(csrf -> csrf.disable())
        .httpBasic(Customizer.withDefaults())
        .exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"No autorizado\"}");
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"Acceso denegado\"}");
                    }
                })
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }
    
    @Bean
    @Order(2)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
        .authorizeHttpRequests(authRequest -> authRequest
            .requestMatchers(HttpMethod.GET, "/articulo/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/articulo/nombre").permitAll()
            .requestMatchers(HttpMethod.POST, "/articulo/actualizacion").permitAll()
            .requestMatchers(HttpMethod.POST, "/articulo").access((authentication, context) -> {
                String ip = context.getRequest().getRemoteAddr();
                if ("45.231.218.92".equals(ip)) {
                    return new AuthorizationDecision(true);
                }
                return new AuthorizationDecision(false);
            })
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/cliente").permitAll()
            .requestMatchers(HttpMethod.PUT, "/cliente/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/codigoVerificacion/validar").permitAll()
            .requestMatchers(HttpMethod.PUT, "/codigoVerificacion/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/cuponDescuento/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/datosTransferencia/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/tarjetaInformacion/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/imagenCarrusel/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/tarjetaRubro/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/envio/cotizarEnvio/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/marca/**").permitAll()
            .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
            .requestMatchers(HttpMethod.POST, "/marca/lista").access((authentication, context) -> {
                String ip = context.getRequest().getRemoteAddr();
                if ("45.231.218.92".equals(ip)) {
                    return new AuthorizationDecision(true);
                }
                return new AuthorizationDecision(false);
            })
            .requestMatchers("/nave/**").hasRole("NAVE")
            .requestMatchers(HttpMethod.GET, "/parametro/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/pedido/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/pedido/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/rubro/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/rubro").access((authentication, context) -> {
                String ip = context.getRequest().getRemoteAddr();
                if ("45.231.218.92".equals(ip)) {
                    return new AuthorizationDecision(true);
                }
                return new AuthorizationDecision(false);
            })
            .requestMatchers(HttpMethod.GET, "/cotizacionEnvio/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/usuario/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/usuario/**").permitAll()
            .anyRequest().hasRole("ADMINISTRADOR")
            )
        .csrf(configuration -> configuration.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response,
                                AuthenticationException authException) throws IOException, ServletException {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"No autorizado\"}");
                        }
                    })
                    .accessDeniedHandler(new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Acceso denegado\"}");
                        }
                    })
        )
        .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), usuarioRepository))
        .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }
}
