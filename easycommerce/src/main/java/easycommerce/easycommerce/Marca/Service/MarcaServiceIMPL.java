package easycommerce.easycommerce.Marca.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Marca.DTOs.MarcaDTOPost;
import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Marca.Repository.MarcaRepository;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;

@Service
public class MarcaServiceIMPL implements MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaServiceIMPL(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
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
    public List<Marca> saveLista(List<Marca> marcas) {
        for (Marca marca : marcas) {
            Optional<Marca> marcaBd = marcaRepository.findById(marca.getCodigo());
            if(marcaBd.isPresent()){
                marca = marcaBd.get();
            }
            else{
                marca.setMostrar(false);
            }    
        }
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
        marcaAGuardar.setCodigo(marca.marca().getCodigo());
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
