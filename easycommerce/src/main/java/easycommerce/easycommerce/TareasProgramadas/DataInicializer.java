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
            Parametro subirArticulos = Parametro.builder().descripcion("SubirArticulos").valor("false").build();
            Parametro subirMarcas = Parametro.builder().descripcion("SubirMarcas").valor("false").build();
            Parametro subirRubros = Parametro.builder().descripcion("SubirRubros").valor("false").build();

            parametroRepository.saveAll(List.of(listaPrecioMayorista,listaPrecioMinorista,montoEnvioGratisGeneral,montoEnvioZona,subirArticulos, subirMarcas, subirRubros));
            System.out.println("Parámetros creados");
        }

        if(usuarioRepository.count() == 0){
            Usuario usuario = new Usuario();
            usuario.setUsername("administrador@la27ferreteria.com.ar");
            usuario.setPassword("$2a$12$7fHP9QDWWjk4exG385Icver5O6cBAsCPD6acIWxRRh04vdJQ0/mEe");
            Optional<Rol> rol = rolRepository.findByDescripcion("ROLE_ADMINISTRADOR");
            if(rol.isPresent()){
                usuario.setRol(rol.get());
            }
            usuario.setValidado(true);

            usuarioRepository.saveAll(List.of(usuario));
        }

        if(cotizacionRepository.count() == 0){
            cotizacionService.obtenerCotizacion("USD");
            System.out.println("Cotizacion Dolar Cargada");
        }
    }  
}
