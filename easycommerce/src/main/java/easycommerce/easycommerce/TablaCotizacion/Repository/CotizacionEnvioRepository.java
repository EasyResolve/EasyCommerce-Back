package easycommerce.easycommerce.TablaCotizacion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.TablaCotizacion.Model.CotizacionEnvio;

@Repository
public interface CotizacionEnvioRepository extends JpaRepository<CotizacionEnvio, Long> {
    
    @Query("SELECT c FROM CotizacionEnvio c WHERE :valor  >= c.pesoDesde AND :valor < pesoHasta")
    Optional<CotizacionEnvio> findByEntreValores(@Param("valor") Double valor);
}
