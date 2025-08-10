package easycommerce.easycommerce.Datos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.TarjetaInformacion;

@Repository
public interface TarjetaInformacionRepository extends JpaRepository<TarjetaInformacion,Long> {

}
