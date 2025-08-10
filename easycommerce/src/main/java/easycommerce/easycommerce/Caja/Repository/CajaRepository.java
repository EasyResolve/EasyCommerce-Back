package easycommerce.easycommerce.Caja.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Caja.Model.Caja;

@Repository
public interface CajaRepository extends JpaRepository<Caja,Long>{

    @Query("SELECT c FROM Caja c WHERE c.descripcion = :descripcion")
    Optional<Caja> findByDescripcion(@Param("descripcion") String descripcion);
}
