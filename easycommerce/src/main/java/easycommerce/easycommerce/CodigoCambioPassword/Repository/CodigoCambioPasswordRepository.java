package easycommerce.easycommerce.CodigoCambioPassword.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.CodigoCambioPassword.Model.CodigoCambioPassword;


@Repository
public interface CodigoCambioPasswordRepository extends JpaRepository<CodigoCambioPassword,Long> {

}
