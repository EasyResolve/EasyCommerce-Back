package easycommerce.easycommerce.Parametros.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Parametros.Model.Parametro;
import easycommerce.easycommerce.Parametros.Repository.ParametroRepository;

@Service
public class ParametroServiceIMPL implements ParametroService{

    private final ParametroRepository parametroRepository;

    public ParametroServiceIMPL(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public List<Parametro> findAll() {
        return parametroRepository.findAll();
    }

    @Override
    public Optional<Parametro> findById(Long id) {
        return parametroRepository.findById(id);
    }

    @Override
    public Optional<Parametro> findByDescripcion(String descripcion) {
        return parametroRepository.findByDescripcion(descripcion);
    }

    @Override
    public Parametro save(Parametro parametro) {
        Optional<Parametro> parametroBd = parametroRepository.findByDescripcion(parametro.getDescripcion());
        if(parametroBd.isPresent()){
            Parametro parametroNuevo = parametroBd.get();
            if(parametro.getDescripcion() != null){
                parametroNuevo.setDescripcion(parametro.getDescripcion());
            }
            if(parametro.getValor() != null){
                parametroNuevo.setValor(parametro.getValor());
            }
            return parametroRepository.save(parametroNuevo);
        }
        else{
            return parametroRepository.save(parametro);
        }
        
    }

    @Override
    public void delete(Long id) {
        parametroRepository.deleteById(id);
    }

    
}
