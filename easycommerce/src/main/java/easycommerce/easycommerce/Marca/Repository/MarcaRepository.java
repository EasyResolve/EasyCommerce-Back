package easycommerce.easycommerce.Marca.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Marca.Model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca,Long> {

    @Query("SELECT m FROM Marca m WHERE m.descripcion = :descripcion")
    Optional<Marca> findByDescripcion(@Param("descripcion") String descripcion);

    Marca findTopByOrderByCodigoDesc();
}
