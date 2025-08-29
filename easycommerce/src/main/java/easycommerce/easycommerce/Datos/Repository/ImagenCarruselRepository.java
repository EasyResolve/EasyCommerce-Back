package easycommerce.easycommerce.Datos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.ImagenCarrusel;

@Repository
public interface ImagenCarruselRepository extends JpaRepository<ImagenCarrusel,Long> {

    @Query("SELECT i FROM ImagenCarrusel i ORDER BY i.orden ASC")
    List<ImagenCarrusel> findAllOrdenadas();
}
