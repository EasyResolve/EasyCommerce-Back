package easycommerce.easycommerce.Rubro.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Rubro.Model.Rubro;

@Repository
public interface RubroRepository extends JpaRepository<Rubro,Long>{

    @Query("SELECT r FROM Rubro r WHERE r.descripcion = :descripcion")
    Optional<Rubro> findByDescripcion(@Param("descripcion") String descripcion);

    List<Rubro> findByCodigoIn(List<Long> codigosRubros);
}
