package easycommerce.easycommerce.Rol.Controller;

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

import easycommerce.easycommerce.Excepciones.EntityAlreadyExistsException;
import easycommerce.easycommerce.Rol.Model.Rol;
import easycommerce.easycommerce.Rol.Service.RolService;



@RestController
@CrossOrigin
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> getAllRoles(){
        List<Rol> roles = rolService.findAll();
        return new ResponseEntity<>(roles,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRol(@PathVariable Long id){
        Optional<Rol> rol = rolService.findById(id);
        if(rol.isPresent()){
            return new ResponseEntity<>(rol.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Rol> saveRol(@RequestBody Rol rol) throws EntityAlreadyExistsException{
        Optional<Rol> rolBd = rolService.findByDescripcion(rol.getDescripcion());
        if(!rolBd.isPresent()){
            Rol rolAGuardar = rolService.save(rol);
            return new ResponseEntity<>(rolAGuardar,HttpStatus.CREATED);
        }
        else{
            throw new EntityAlreadyExistsException("Ya existe un rol con la descripcion: " + rol.getDescripcion());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id){
        Optional<Rol> rol = rolService.findById(id);
        if(rol.isPresent()){
            rolService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
