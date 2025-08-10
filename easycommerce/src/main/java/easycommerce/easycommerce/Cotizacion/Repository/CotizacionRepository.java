package easycommerce.easycommerce.Cotizacion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Cotizacion.Model.Cotizacion;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion,Long> {

    @Query("SELECT c FROM Cotizacion c WHERE c.moneda = :moneda")
    Optional<Cotizacion> findByMoneda(@Param("moneda") String moneda);
}
