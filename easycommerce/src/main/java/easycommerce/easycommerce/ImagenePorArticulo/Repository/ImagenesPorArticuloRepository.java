package easycommerce.easycommerce.ImagenePorArticulo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.ImagenePorArticulo.Models.ImagenesPorArticulo;

@Repository
public interface ImagenesPorArticuloRepository extends JpaRepository<ImagenesPorArticulo, Long>{

    @Query("SELECT i FROM ImagenesPorArticulo i WHERE i.nombreImagen = :nombre")
    Optional<ImagenesPorArticulo> findByDescripcion(@Param("nombre") String nombre);
}
