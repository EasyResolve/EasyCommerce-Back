package easycommerce.easycommerce.Rol.Service;

import java.util.List;
import java.util.Optional;

import easycommerce.easycommerce.Rol.Model.Rol;

public interface RolService {
    List<Rol> findAll();
    Optional<Rol> findById(Long id);
    Rol save(Rol rol);
    void delete(Long id);
    Optional<Rol> findByDescripcion(String descripcion);
}
