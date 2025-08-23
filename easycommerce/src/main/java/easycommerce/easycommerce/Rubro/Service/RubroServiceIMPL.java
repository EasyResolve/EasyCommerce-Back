package easycommerce.easycommerce.Rubro.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Articulo.Repository.ArticuloRepository;
import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPost;
import easycommerce.easycommerce.Rubro.DTOs.RubroDTOPut;
import easycommerce.easycommerce.Rubro.Model.Rubro;
import easycommerce.easycommerce.Rubro.Repository.RubroRepository;
import easycommerce.easycommerce.SubRubro.Model.SubRubro;
import easycommerce.easycommerce.SubRubro.Repository.SubRubroRepository;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;

@Service
public class RubroServiceIMPL implements RubroService {

    private final RubroRepository rubroRepository;
    private final SubRubroRepository subRubroRepository;
    private final ArticuloRepository articuloRepository;

    public RubroServiceIMPL(RubroRepository rubroRepository, SubRubroRepository subRubroRepository,
            ArticuloRepository articuloRepository) {
        this.rubroRepository = rubroRepository;
        this.subRubroRepository = subRubroRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<Rubro> findAll() {
        List<Long> codigosRubros = articuloRepository.findUsedRubroCode();
        List<Long> codigosSubrubros = articuloRepository.findUsedSubRubroCode();

        Set<Long> subrubrosUsados = new HashSet<>(codigosSubrubros);

        List<Rubro> rubros = rubroRepository.findByCodigoIn(codigosRubros);

        // Filtrar subrubros no referenciados
        for (Rubro rubro : rubros) {
            rubro.setSubRubros(
                rubro.getSubRubros().stream()
                    .filter(sr -> subrubrosUsados.contains(sr.getCodigo()))
                    .toList()
            );
        }

        return rubros;
    }

    @Override
    public Optional<Rubro> findById(Long id){
        return rubroRepository.findById(id);
    }

    @Override
    public List<Rubro> saveList(List<RubroDTOPost> rubros) {
        //Creo un hash map para poner los prefijos de los rubros que creo
        Map<String,Rubro> rubrosAGuardar = new HashMap<>();
        //Ordeno los rubros que me llegan para guardar por numeros de menor a mayor
        Collections.sort(rubros, Comparator.comparing(RubroDTOPost::Codrubro));
        //Recorro todos los rubros que me llegan para guardar
        for (RubroDTOPost rubro : rubros) {
            //Creo un objeto Rubro y le paso los datos que me llegan
            Rubro rubroAGuardar = new Rubro();
            rubroAGuardar.setCodigo(rubro.Codrubro());
            rubroAGuardar.setDescripcion(rubro.Descripcion());
            //Creo una lista con los subrubros para un rubro determinado
            List<SubRubro> subRubrosNuevo = new ArrayList<>();
            rubroAGuardar.setSubRubros(subRubrosNuevo);
            
            //Obtengo el codigo de rubro que me llegan desde el json
            String codigo = rubroAGuardar.getCodigo().toString();
            //Obtengo los ultimos tres numeros
            String prefijo = codigo.substring(0, 3);

            //Compara si termina en 000 para saber si es un rubro o un subrubro
            //Si termina en 000 es un rubro
            if(codigo.endsWith("000")){
                //Busco el rubro en la base de datos
                Optional<Rubro> rubroBd = rubroRepository.findById(rubroAGuardar.getCodigo());
                //Si el rubro existe
                if(rubroBd.isPresent()){
                    //Seteo el codigo del rubro y lo inserto al hashmap
                    rubroAGuardar.setCodigo(rubroBd.get().getCodigo());
                    rubroAGuardar.setVisible(rubroBd.get().getVisible());
                    rubroAGuardar.setImagen(rubroBd.get().getImagen());
                    rubrosAGuardar.put(prefijo, rubroAGuardar);
                }
                //si no existe el rubro lo inserto al hashmap
                else{
                    rubroAGuardar.setVisible(false);
                    rubroAGuardar.setImagen(null);
                    rubrosAGuardar.put(prefijo, rubroAGuardar);
                    
                }
            }
            //Si es un subrubro, es decir que no termina en 000
            else{
                //Busco el subrubro en la base de datos
                Optional<SubRubro> subRubro = subRubroRepository.findByCodigo(codigo);
                //Si existe el subrubro
                if(subRubro.isPresent()){
                    //Creo el subrubro en un objeto con los datos que me llegan de la bd y lo agrego a la lista de sub rubros
                    SubRubro subRubroAGuardar = new SubRubro(subRubro.get().getCodigo(), rubroAGuardar.getDescripcion());
                    //Si el prefijo del rubro que estoy guardando existe en el hashmap lo busco y agrego los subrubros a ese rubro
                    if(rubrosAGuardar.containsKey(prefijo)){
                        rubrosAGuardar.get(prefijo).agregarSubRubro(subRubroAGuardar);
                    }
                }
                //Si el subrubro no existe
                else{
                    //Creo el subrubro con lo que me esta llegando por parametro
                    SubRubro subRubroAGuardar = new SubRubro(rubroAGuardar.getCodigo(), rubroAGuardar.getDescripcion());
                    //Busco si existe un rubro en el hashmap con el prefijo que estoy tratando de guardar
                    //Si existe agrego los subrubros al rubro con el prefijo indicado
                    if(rubrosAGuardar.containsKey(prefijo)){
                        rubrosAGuardar.get(prefijo).agregarSubRubro(subRubroAGuardar);
                    }
                }
            }
        }
        //Una vez que recorro todo hago una lista con los objetos que se encuentran en el hashmap de acuerdo a su prefijo o valor
        List<Rubro> rubrosConSubRubros = new ArrayList<>(rubrosAGuardar.values());
        //Guardo los rubros con sus respectivos sub rubros
        return rubroRepository.saveAll(rubrosConSubRubros);
        
    }

    @Override
    public void delete(Long id) {
        rubroRepository.deleteById(id);
    }

    @Override
    public Rubro save(RubroDTOPut rubro) throws IOException {
        Rubro rubroGuardado = rubroRepository.save(rubro.rubro());
        if(rubro.file() != null){
            List<MultipartFile> files = new ArrayList<>();
            files.add(rubro.file());
            List<String> rutas = ImageStorageUtil.saveImages(files, "imagenes/rubros/rubro_" + rubroGuardado.getCodigo());
            if(!rutas.isEmpty()){
                rubroGuardado.setImagen(rutas.getFirst());
            }
            rubroGuardado = rubroRepository.save(rubroGuardado);
        }
        return rubroGuardado;
    }

}
