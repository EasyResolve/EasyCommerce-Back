package easycommerce.easycommerce.CodigoVerificacion.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.CodigoVerificacion.Model.CodigoVerificacion;

@Repository
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion,Long> {
}
