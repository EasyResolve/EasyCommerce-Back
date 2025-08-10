package easycommerce.easycommerce.Parametros.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Parametros.Model.Parametro;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro,Long> {

    @Query("SELECT p FROM Parametro p WHERE p.descripcion = :descripcion")
    Optional<Parametro> findByDescripcion(@Param("descripcion") String descripcion);
}
