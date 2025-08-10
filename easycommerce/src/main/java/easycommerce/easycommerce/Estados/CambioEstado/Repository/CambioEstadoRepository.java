package easycommerce.easycommerce.Estados.CambioEstado.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;

@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado,Long>{

}
