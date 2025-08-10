package easycommerce.easycommerce.Rol.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Rol.Model.Rol;
import easycommerce.easycommerce.Rol.Repository.RolRepository;

@Service
public class RolServiceIMPL implements RolService{
    
    private final RolRepository rolRepository;

    public RolServiceIMPL(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void delete(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public Optional<Rol> findByDescripcion(String descripcion) {
        return rolRepository.findByDescripcion(descripcion);
    }
    
}
