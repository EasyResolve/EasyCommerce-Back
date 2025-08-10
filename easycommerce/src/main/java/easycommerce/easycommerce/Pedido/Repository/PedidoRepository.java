package easycommerce.easycommerce.Pedido.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Pedido.Model.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long>{
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId")
    List<Pedido> findByCliente(@Param("clienteId") Long clienteId);

    @Query("SELECT p FROM Pedido p WHERE MONTH(p.fechaCreacion) = MONTH(CURRENT_DATE) AND YEAR(p.fechaCreacion) = YEAR(CURRENT_DATE)")
    List<Pedido> findPedidosDelMesActual();

    /*@Query("SELECT p FROM Pedido p " +
       "WHERE (:fechaInicio IS NULL OR CAST(p.fechaCreacion AS DATE) >= CAST(:fechaInicio AS DATE)) " +
       "AND (:fechaFin IS NULL OR CAST(p.fechaCreacion AS DATE) <= CAST(:fechaFin AS DATE)) " +
       "AND (:estado IS NULL OR p.estadoActual.descripcion = :estado) " +
       "AND (:documento IS NULL OR p.cliente.documento = :documento)")
    List<Pedido> findPedidosByFilters(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("estado") String estado, @Param("documento") String documento);
    */
    @Query("SELECT p FROM Pedido p " +
       "WHERE (:fechaInicio IS NULL OR FUNCTION('DATE', p.fechaCreacion) >= :fechaInicio) " +
       "AND (:fechaFin IS NULL OR FUNCTION('DATE', p.fechaCreacion) <= :fechaFin) " +
       "AND (:estado IS NULL OR LOWER(p.estadoActual.descripcion) = LOWER(:estado)) " +
       "AND (:documento IS NULL OR p.cliente.documento = :documento)")
    List<Pedido> findPedidosByFilters(@Param("fechaInicio") LocalDate fechaInicio, 
                                  @Param("fechaFin") LocalDate fechaFin, 
                                  @Param("estado") String estado, 
                                  @Param("documento") String documento);
}
