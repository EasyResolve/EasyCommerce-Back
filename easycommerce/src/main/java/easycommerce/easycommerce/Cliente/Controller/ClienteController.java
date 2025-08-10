package easycommerce.easycommerce.Cliente.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cliente.Service.ClienteService;
import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;

@RestController
@CrossOrigin
@RequestMapping("/cliente")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes(){
        List<Cliente> clientes = clienteService.findAll();
        return new ResponseEntity<>(clientes,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable Long id){
        Optional<Cliente> cliente = clienteService.findById(id);
        if(cliente.isPresent()){
            return new ResponseEntity<>(cliente.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente) throws EntityAlreadyExistsException{
        Cliente clienteGuardado = clienteService.save(cliente);
        return new ResponseEntity<>(clienteGuardado,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente){
        Optional<Cliente> clienteBd = clienteService.findById(cliente.getId());
        if(clienteBd.isPresent()){
            Cliente clienteModificado = clienteBd.get();
            if(cliente.getApellido() != null){
                clienteModificado.setApellido(cliente.getApellido());
            }
            if(cliente.getCondicionIva() != null){
                clienteModificado.setCondicionIva(cliente.getCondicionIva());
            }
            if(cliente.getDocumento() != null){
                clienteModificado.setDocumento(cliente.getDocumento());
            }
            if(cliente.getEmail() != null){
                clienteModificado.setEmail(cliente.getEmail());
            }
            if(cliente.getNombre() != null){
                clienteModificado.setNombre(cliente.getNombre());
            }
            if(cliente.getTelefono() != null){
                clienteModificado.setTelefono(cliente.getTelefono());
            }
            if(cliente.getTipoCliente() != null){
                clienteModificado.setTipoCliente(cliente.getTipoCliente());
            }
            clienteModificado = clienteService.updateCliente(cliente);
            return new ResponseEntity<>(clienteModificado,HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id){
        Optional<Cliente> cliente = clienteService.findById(id);
        if(cliente.isPresent()){
            clienteService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
