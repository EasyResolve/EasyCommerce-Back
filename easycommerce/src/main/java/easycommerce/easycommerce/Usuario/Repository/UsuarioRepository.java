package easycommerce.easycommerce.Usuario.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Usuario.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    @Query("SELECT u FROM Usuario u WHERE u.username = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.cliente.id = :clienteId")
    Optional<Usuario> findByCliente(@Param("clienteId") Long clienteId);
}
