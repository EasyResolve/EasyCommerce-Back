package easycommerce.easycommerce.Datos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Datos.Model.ImagenCarrusel;

@Repository
public interface ImagenCarruselRepository extends JpaRepository<ImagenCarrusel,Long> {

}
