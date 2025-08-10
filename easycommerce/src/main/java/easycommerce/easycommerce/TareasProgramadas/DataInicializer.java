package easycommerce.easycommerce.TareasProgramadas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import easycommerce.easycommerce.Cotizacion.Repository.CotizacionRepository;
import easycommerce.easycommerce.Cotizacion.Service.CotizacionService;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMayorista;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMinorista;
import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;
import easycommerce.easycommerce.Rol.Model.Rol;
import easycommerce.easycommerce.Rol.Repository.RolRepository;
import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;
import easycommerce.easycommerce.TablaCotizacion.Repository.CotizacionEnvioRepository;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;



@Component
public class DataInicializer implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final ParametroRepository parametroRepository;
    private final UsuarioRepository usuarioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionService cotizacionService;
    private final CotizacionEnvioRepository cotizacionEnvioRepository;
    
    public DataInicializer(RolRepository rolRepository, ParametroRepository parametroRepository,
            UsuarioRepository usuarioRepository, CotizacionRepository cotizacionRepository,
            CotizacionService cotizacionService, CotizacionEnvioRepository cotizacionEnvioRepository) {
        this.rolRepository = rolRepository;
        this.parametroRepository = parametroRepository;
        this.usuarioRepository = usuarioRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.cotizacionService = cotizacionService;
        this.cotizacionEnvioRepository = cotizacionEnvioRepository;
    }




    @Override
    public void run(String... args) throws Exception {
        if(rolRepository.count() == 0){
            Rol roleAdmin = Rol.builder().descripcion("ROLE_ADMINISTRADOR").build();
            Rol roleCliente = Rol.builder().descripcion("ROLE_CLIENTE").build();
            Rol roleNave = Rol.builder().descripcion("ROLE_NAVE").build();
            rolRepository.saveAll(List.of(roleAdmin,roleCliente,roleNave));
            System.out.println("Roles creados");
        }

        if(parametroRepository.count() == 0){
            Parametro listaPrecioMayorista = new Parametro();
            listaPrecioMayorista.setDescripcion("listaPrecioMayorista");
            listaPrecioMayorista.setListaPrecioMayorista(ListaPrecioMayorista.Lista2);

            Parametro listaPrecioMinorista = new Parametro();
            listaPrecioMinorista.setDescripcion("listaPrecioMinorista");
            listaPrecioMinorista.setListaPrecioMinorista(ListaPrecioMinorista.Lista2);

            Parametro montoEnvioGratisGeneral = Parametro.builder().descripcion("montoEnvioGratisGeneral").valor("250000").build();
            Parametro montoEnvioZona = Parametro.builder().descripcion("montoEnvioZonas").valor("15000").build();

            parametroRepository.saveAll(List.of(listaPrecioMayorista,listaPrecioMinorista,montoEnvioGratisGeneral,montoEnvioZona));
            System.out.println("Parámetros creados");
        }

        if(usuarioRepository.count() == 0){
            Usuario usuario = new Usuario();
            usuario.setUsername("gnratto@gnr.ar");
            usuario.setPassword("$2a$12$P5pFU1kzFKYBifNhxUsewui2WJWrqXB71XEJyA7pddJCWQ25kDiwC");
            Optional<Rol> rol = rolRepository.findByDescripcion("ROLE_ADMINISTRADOR");
            if(rol.isPresent()){
                usuario.setRol(rol.get());
            }
            usuario.setValidado(true);

            Usuario usuario2 = new Usuario();
            usuario2.setUsername("navePasarelaProd");
            usuario2.setPassword("$2a$12$739TFOt7QynkI1npHQoV2.3wqa1MhvpFB7dX//fkArIlfLuOzCGSq");
            Optional<Rol> rol2 = rolRepository.findByDescripcion("ROLE_NAVE");
            if(rol.isPresent()){
                usuario2.setRol(rol2.get());
            }
            usuario2.setValidado(true);
            usuarioRepository.saveAll(List.of(usuario,usuario2));
        }

        if(cotizacionRepository.count() == 0){
            cotizacionService.obtenerCotizacion("USD");
            System.out.println("Cotizacion Dolar Cargada");
        }

        if(cotizacionEnvioRepository.count() == 0){
            CotizacionEnvio cotizacion1 = new CotizacionEnvio();
            cotizacion1.setPesoDesde(0.0);
            cotizacion1.setPesoHasta(1000.00);
            cotizacion1.setMontoEnvio(BigDecimal.valueOf(14000));
            CotizacionEnvio cotizacion2 = new CotizacionEnvio();
            cotizacion2.setPesoDesde(1000.00);
            cotizacion2.setPesoHasta(5000.00);
            cotizacion2.setMontoEnvio(BigDecimal.valueOf(16400));
            CotizacionEnvio cotizacion3 = new CotizacionEnvio();
            cotizacion3.setPesoDesde(5000.00);
            cotizacion3.setPesoHasta(10000.00);
            cotizacion3.setMontoEnvio(BigDecimal.valueOf(22000.00));
            CotizacionEnvio cotizacion4 = new CotizacionEnvio();
            cotizacion4.setPesoDesde(10.000);
            cotizacion4.setPesoHasta(15000.00);
            cotizacion4.setMontoEnvio(BigDecimal.valueOf(27300));
            CotizacionEnvio cotizacion5 = new CotizacionEnvio();
            cotizacion5.setPesoDesde(15000.00);
            cotizacion5.setPesoHasta(20000.00);
            cotizacion5.setMontoEnvio(BigDecimal.valueOf(32100.00));
            CotizacionEnvio cotizacion6 = new CotizacionEnvio();
            cotizacion6.setPesoDesde(20.000);
            cotizacion6.setPesoHasta(25000.00);
            cotizacion6.setMontoEnvio(BigDecimal.valueOf(38600));
            cotizacionEnvioRepository.saveAll(List.of(cotizacion1, cotizacion2, cotizacion3, cotizacion4, cotizacion5, cotizacion6));
            System.out.println("Cotizaciones De Envios Por Peso Cargadas");
        }
    }

    
}
