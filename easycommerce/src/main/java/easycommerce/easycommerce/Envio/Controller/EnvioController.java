package easycommerce.easycommerce.Envio.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Envio.DTOs.DTOCotizarEnvio;
import easycommerce.easycommerce.Envio.Model.EnvioConDatos;
import easycommerce.easycommerce.Envio.Service.CotizarEnvioService;


@RestController
@CrossOrigin
@RequestMapping("/envio")
public class EnvioController {
    
    private final CotizarEnvioService cotizarEnvioService;

    public EnvioController(CotizarEnvioService cotizarEnvioService) {
        this.cotizarEnvioService = cotizarEnvioService;
    }

    @PostMapping("/cotizarEnvio")
    public ResponseEntity<BigDecimal> cotizarEnvio(@RequestBody DTOCotizarEnvio cotizacionEnvio) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != "anonymousUser") {
            username = authentication.getName();
        }
        EnvioConDatos envio = cotizarEnvioService.cotizarEnvio(cotizacionEnvio,username);
        BigDecimal montoEnvio = envio.costoEnvio();
        return new ResponseEntity<>(montoEnvio, HttpStatus.OK);
    }
}
