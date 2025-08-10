package easycommerce.easycommerce.Envio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Envio.Model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long>{

}
