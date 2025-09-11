package easycommerce.easycommerce.Marca.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Marca.DTOs.MarcaDTOPost;
import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Marca.Repository.MarcaRepository;
import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;

@Service
public class MarcaServiceIMPL implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final ParametroRepository parametroRepository;

    public MarcaServiceIMPL(MarcaRepository marcaRepository, ParametroRepository parametroRepository) {
        this.marcaRepository = marcaRepository;
        this.parametroRepository = parametroRepository;
    }

    @Override
    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    @Override
    public Optional<Marca> findByCodigo(Long codigo) {
        return marcaRepository.findById(codigo);
    }

    @Override
    public List<Marca> saveLista(List<Marca> marcas) throws Exception {
        Optional<Parametro> parametroBd = parametroRepository.findByDescripcion("SubirMarcas");
        if(!parametroBd.isPresent()){
            throw new NoSuchElementException("No se encontro el parametro especificado");
        }
        if(parametroBd.get().getValor().equals("false")){
            throw new Exception("No se pudieron subir las marcas porque no tiene permisos");
        }
        for (Marca marca : marcas) {
            Optional<Marca> marcaBd = marcaRepository.findById(marca.getCodigo());
            if(marcaBd.isPresent()){
                marca = marcaBd.get();
            }
            else{
                marca.setMostrar(false);
            }    
        }
        parametroBd.get().setValor("false");
        parametroRepository.save(parametroBd.get());
        return marcaRepository.saveAll(marcas);
    }

    @Override
    public void delete(Long codigo) throws IOException {
        Optional<Marca> marcaBd = marcaRepository.findById(codigo);
        if(marcaBd.isPresent()){
            List<String> rutaImagen = List.of(marcaBd.get().getUrlImagen());
            if(!rutaImagen.isEmpty()){
                ImageStorageUtil.deleteImage(rutaImagen);
            } 
        }
        marcaRepository.deleteById(codigo);
    }

    @Override
    public Marca saveMarca(MarcaDTOPost marca) throws IOException {
        Marca marcaAGuardar = new Marca();
        if(marca.marca().getCodigo() == null){
            Marca ultimaMarca = marcaRepository.findTopByOrderByCodigoDesc();
            if(ultimaMarca != null){
                marcaAGuardar.setCodigo(ultimaMarca.getCodigo() + 1);
            }
            else{
                marcaAGuardar.setCodigo(Long.valueOf(1));
            }
        }
        else{
            marcaAGuardar.setCodigo(marca.marca().getCodigo());
        } 
        marcaAGuardar.setDescripcion(marca.marca().getDescripcion());
        marcaAGuardar.setUrlImagen(marca.marca().getUrlImagen());
        if(marca.marca().isMostrar()){
            marcaAGuardar.setMostrar(true);
        }
        else{
            marcaAGuardar.setMostrar(false);
        }
        marcaAGuardar = marcaRepository.save(marcaAGuardar);
        if(marca.file() != null){
            List<MultipartFile> files = new ArrayList<>();
            files.add(marca.file());
            List<String> rutas = ImageStorageUtil.saveImages(files, "imagenes/marca/marca_" + marcaAGuardar.getCodigo());
            if(!rutas.isEmpty()){
                marcaAGuardar.setUrlImagen(rutas.getFirst());
            }
            marcaAGuardar = marcaRepository.save(marcaAGuardar);
        }
        return marcaAGuardar;
    }
}
