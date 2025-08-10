package easycommerce.easycommerce.Datos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Datos.Model.DatosTransferencia;
import easycommerce.easycommerce.Datos.Repository.DatosTransferenciaRepository;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;

@Service
public class DatosTransferenciaServiceIMPL implements DatosTransferenciaService {

    private final DatosTransferenciaRepository datosTransferenciaRepository;

    
    public DatosTransferenciaServiceIMPL(DatosTransferenciaRepository datosTransferenciaRepository) {
        this.datosTransferenciaRepository = datosTransferenciaRepository;
    }

    @Override
    public List<DatosTransferencia> findAll() {
        return datosTransferenciaRepository.findAll();
    }

    @Override
    public Optional<DatosTransferencia> findById(Long id) {
        return datosTransferenciaRepository.findById(id);
    }

    @Override
    public DatosTransferencia save(DatosTransferencia datosTransferencia) {
        List<DatosTransferencia> datos = datosTransferenciaRepository.findAll();
        if(datos.size() > 0){
            DatosTransferencia transferencia = datos.getFirst();
            if(datosTransferencia.getAlias() != null){
                transferencia.setAlias(datosTransferencia.getAlias());
            }
            if(datosTransferencia.getBanco() != null){
                transferencia.setBanco(datosTransferencia.getBanco());
            }
            if(datosTransferencia.getCbu() != null){
                transferencia.setCbu(datosTransferencia.getCbu());
            }
            if(datosTransferencia.getTitular() != null){
                transferencia.setTitular(datosTransferencia.getTitular());
            }
            if(datosTransferencia.getEmail() != null){
                transferencia.setEmail(datosTransferencia.getEmail());
            }
            return datosTransferenciaRepository.save(datosTransferencia);
        }
        else{
            return datosTransferenciaRepository.save(datosTransferencia);
        }
        
    }

    @Override
    public void delete(Long id) throws NoSuchElementException {
        Optional<DatosTransferencia> datos = datosTransferenciaRepository.findById(id);
        if(datos.isPresent()){
            datosTransferenciaRepository.delete(datos.get());
        }
        else{
            throw new NoSuchElementException("No se encuentra una cuenta con el id: " + id);
        }
    }

}
