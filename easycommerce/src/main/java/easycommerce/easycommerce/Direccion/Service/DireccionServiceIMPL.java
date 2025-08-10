package easycommerce.easycommerce.Direccion.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Cliente.Repository.ClienteRepository;
import easycommerce.easycommerce.Direccion.Model.Direccion;
import easycommerce.easycommerce.Direccion.Repository.DireccionRepository;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;


@Service
public class DireccionServiceIMPL implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;
    public DireccionServiceIMPL(DireccionRepository direccionRepository, ClienteRepository clienteRepository) {
        this.direccionRepository = direccionRepository;
        this.clienteRepository = clienteRepository;
    }
    @Override
    public List<Direccion> findAll() {
        return direccionRepository.findAll();
    }
    @Override
    public Optional<Direccion> findById(Long id) {
        return direccionRepository.findById(id);
    }
    @Override
    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }
    @Override
    public void delete(Long id) {
        direccionRepository.deleteById(id);
    }
    @Override
    public List<Direccion> findByCliente(Long id) throws NoSuchElementException {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if(cliente.isPresent()){
            return cliente.get().getDirecciones();
        }
        else{
            throw new NoSuchElementException("No existe un cliente con el id " + id);
        }
    }
    @Override
    public Optional<Direccion> verify(String calle, String numero, String ciudad, String codigoPostal, String provincia, String pais,
            Long idCliente) throws NoSuchElementException {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if(cliente.isPresent()){
            return cliente.get().getDirecciones().stream()
            .filter(dir -> dir.getCalle().equalsIgnoreCase(calle) &&
                           dir.getNumero().equalsIgnoreCase(numero) &&
                           dir.getCiudad().equalsIgnoreCase(ciudad) &&
                           dir.getCodigoPostal().equalsIgnoreCase(codigoPostal) &&
                           dir.getProvincia().equalsIgnoreCase(provincia) &&
                           dir.getPais().equalsIgnoreCase(pais))
            .findFirst();
        }
        else{
            throw new NoSuchElementException("No existe un cliente con el id: " + idCliente);
        }
    }

    
}
