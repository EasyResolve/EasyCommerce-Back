package easycommerce.easycommerce.Estados.Estado.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Estados.Estado.Model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Long>{

    @Query("SELECT e FROM Estado e WHERE e.descripcion = :descripcion")
    Optional<Estado> findByDescripcion(@Param("descripcion") String descripcion);
}
