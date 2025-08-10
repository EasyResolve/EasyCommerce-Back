package easycommerce.easycommerce.SubRubro.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.SubRubro.Model.SubRubro;

@Repository
public interface SubRubroRepository extends JpaRepository<SubRubro,Long>{
    @Query("SELECT sr FROM SubRubro sr WHERE sr.codigo = :codigo")
    Optional<SubRubro> findByCodigo(@Param("codigo") String codigo);

    @Query("SELECT sr FROM SubRubro sr WHERE sr.descripcion = :descripcion")
    Optional<SubRubro> findByDescripcion(@Param("descripcion") String descripcion);
}
