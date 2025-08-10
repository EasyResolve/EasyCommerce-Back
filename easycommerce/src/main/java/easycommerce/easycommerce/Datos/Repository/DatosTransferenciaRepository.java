package easycommerce.easycommerce.Datos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.DatosTransferencia;

@Repository
public interface DatosTransferenciaRepository extends JpaRepository<DatosTransferencia,Long> {

}
