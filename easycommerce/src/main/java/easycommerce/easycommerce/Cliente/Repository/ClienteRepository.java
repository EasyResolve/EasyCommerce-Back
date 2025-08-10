package easycommerce.easycommerce.Cliente.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Cliente.Model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long>{
    @Query("SELECT c FROM Cliente c WHERE c.documento = :documento")
    Optional<Cliente> findByDocumento(@Param("documento") String documento);

}
