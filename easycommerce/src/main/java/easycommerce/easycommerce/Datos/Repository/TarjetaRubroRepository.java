package easycommerce.easycommerce.Datos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.TarjetaRubro;

@Repository
public interface TarjetaRubroRepository extends JpaRepository<TarjetaRubro,Long> {

}
