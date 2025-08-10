package easycommerce.easycommerce.Rol.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Rol.Model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

    @Query("SELECT r FROM Rol r WHERE r.descripcion = :descripcion")
    Optional<Rol> findByDescripcion(@Param("descripcion") String descripcion);
}
