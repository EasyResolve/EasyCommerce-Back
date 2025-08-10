package easycommerce.easycommerce.Datos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Datos.Model.DatosTransferencia;
import easycommerce.easycommerce.Datos.Service.DatosTransferenciaService;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;



@RestController
@CrossOrigin
@RequestMapping("/datosTransferencia")
public class DatosTransferenciaController {

    private final DatosTransferenciaService datosTransferenciaService;

    public DatosTransferenciaController(DatosTransferenciaService datosTransferenciaService) {
        this.datosTransferenciaService = datosTransferenciaService;
    }

    @GetMapping
    public ResponseEntity<DatosTransferencia> getDatosTransferencia(){
        List<DatosTransferencia> datos = datosTransferenciaService.findAll();
        DatosTransferencia transferencia = datos.getFirst();
        return new ResponseEntity<>(transferencia,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosTransferencia> getDatosTransferencia(@PathVariable Long id) throws NoSuchElementException{
        Optional<DatosTransferencia> datosBd = datosTransferenciaService.findById(id);
        if(datosBd.isPresent()){
            return new ResponseEntity<>(datosBd.get(),HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No se encuentran los datos de transferencia con id: " + id);
        }
        
    }

    @PostMapping
    public ResponseEntity<DatosTransferencia> saveDatosTransferencia(@RequestBody DatosTransferencia datosTransferencia){
        DatosTransferencia datos = datosTransferenciaService.save(datosTransferencia);
        return new ResponseEntity<>(datos,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatosTransferencia(@PathVariable Long id) throws NoSuchElementException{
        Optional<DatosTransferencia> datosBd = datosTransferenciaService.findById(id);
        if(datosBd.isPresent()){
            datosTransferenciaService.delete(datosBd.get().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            throw new NoSuchElementException("No se encuentran los datos de transferencia con id: " + id);
        }
        
        
    }
}
