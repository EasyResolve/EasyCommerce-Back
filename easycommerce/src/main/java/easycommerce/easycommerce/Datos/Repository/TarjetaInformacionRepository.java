package easycommerce.easycommerce.Datos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.TarjetaInformacion;

@Repository
public interface TarjetaInformacionRepository extends JpaRepository<TarjetaInformacion,Long> {

    @Query("SELECT t FROM TarjetaInformacion t ORDER BY t.orden ASC")
    List<TarjetaInformacion> findAllOrdenadas();
}
