package easycommerce.easycommerce.CuponDescuento.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.CuponDescuento.Model.CuponDescuento;

@Repository
public interface CuponDescuentoRepository extends JpaRepository<CuponDescuento,Long>{

    @Query("SELECT c FROM CuponDescuento c WHERE c.codigo = :codigo")
    Optional<CuponDescuento> findByCodigo(@Param("codigo") String codigo);
}
