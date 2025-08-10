package easycommerce.easycommerce.Datos.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import easycommerce.easycommerce.Datos.Model.TarjetaInformacion;
import easycommerce.easycommerce.Datos.Model.TarjetaInformacionDTOPost;
import easycommerce.easycommerce.Datos.Repository.TarjetaInformacionRepository;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.TareasProgramadas.ImageStorageUtil;

@Service
public class TarjetaInformacionServiceIMPL implements TarjetaInformacionService {

    private final TarjetaInformacionRepository tarjetaInformacionRepository;

    public TarjetaInformacionServiceIMPL(TarjetaInformacionRepository tarjetaInformacionRepository) {
        this.tarjetaInformacionRepository = tarjetaInformacionRepository;
    }

    @Override
    public List<TarjetaInformacion> findAll() {
        return tarjetaInformacionRepository.findAll();
    }

    @Override
    public Optional<TarjetaInformacion> findById(Long id) {
        return tarjetaInformacionRepository.findById(id);
    }

    @Override
    public TarjetaInformacion save(TarjetaInformacionDTOPost tarjetaInformacion) throws NoSuchElementException, IOException {
        TarjetaInformacion tarjetaNueva = new TarjetaInformacion();
        if(tarjetaInformacion.informacion().getId() != null){
            Optional<TarjetaInformacion> tarjetaBd = tarjetaInformacionRepository.findById(tarjetaInformacion.informacion().getId());
            if(tarjetaBd.isPresent()){
                tarjetaNueva = tarjetaBd.get();
            }
            else{
                throw new NoSuchElementException("No se encontro una tarjeta informacion con el id: " + tarjetaInformacion.informacion().getId());
            }
            if(tarjetaInformacion.informacion().getIcono() != null){
                tarjetaNueva.setIcono(tarjetaInformacion.informacion().getIcono());
            }
            if(tarjetaInformacion.informacion().getTexto() != null){
                tarjetaNueva.setTexto(tarjetaInformacion.informacion().getTexto());
            }
            tarjetaNueva = tarjetaInformacionRepository.save(tarjetaNueva);

            if(tarjetaInformacion.file() != null){
                List<MultipartFile> files = new ArrayList<>();
                files.add(tarjetaInformacion.file());
                List<String> rutas = ImageStorageUtil.saveImages(files, "imagenes/tarjetaInformacion/tarjetaInformacion_" + tarjetaNueva.getId());
                if(!rutas.isEmpty()){
                    tarjetaNueva.setIcono(rutas.getFirst());
                }
                tarjetaNueva = tarjetaInformacionRepository.save(tarjetaNueva);
            }
        }
        else{
            tarjetaNueva.setTexto(tarjetaInformacion.informacion().getTexto());
            tarjetaNueva.setIcono(tarjetaInformacion.informacion().getIcono());
            tarjetaNueva = tarjetaInformacionRepository.save(tarjetaNueva);
        }
        return tarjetaNueva;
    }

    @Override
    public void delete(Long id) throws IOException {
        Optional<TarjetaInformacion> tarjetaInformacion = tarjetaInformacionRepository.findById(id);
        if(tarjetaInformacion.isPresent()){
            List<String> rutaImagen = List.of(tarjetaInformacion.get().getIcono());
            if(!rutaImagen.isEmpty()){
                ImageStorageUtil.deleteImage(rutaImagen);
            }
            tarjetaInformacionRepository.deleteById(id);
        }
    }

    
}
