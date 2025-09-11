package easycommerce.easycommerce.Articulo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Articulo.Model.Articulo;


@Repository
public interface ArticuloRepository extends JpaRepository<Articulo,Long> {

    @Query("SELECT a FROM Articulo a WHERE a.rubro.id = :rubroId")
    List<Articulo> findArticulosByRubroId(@Param("rubroId") Long rubroId);

    @Query("SELECT a FROM Articulo a WHERE a.stockActual > 0 AND a.activo = true")
    List<Articulo> findArticulosByCantidad();

    @Query("SELECT a FROM Articulo a WHERE a.nombre = :nombre")
    Optional<Articulo> findArticulosByNombre(@Param("nombre") String nombre);

    @Query("SELECT DISTINCT a.rubro.codigo FROM Articulo a WHERE a.activo = true and a.stockActual > 0")
    List<Long> findUsedRubroCode();

    @Query("SELECT DISTINCT a.subRubro.codigo FROM Articulo a WHERE a.activo = true and a.stockActual > 0")
    List<Long> findUsedSubRubroCode();

    @Query("SELECT i FROM Articulo a JOIN a.urlImagenes i WHERE i LIKE CONCAT('%', :nombreImagen) ESCAPE '\\'")
    Optional<String> buscarRutaPorNombreImagen(@Param("nombreImagen") String nombreImagen);

    @Query("SELECT a FROM Articulo a WHERE a.stockActual = 0")
    List<Articulo> findArticulosSinStock();
}
