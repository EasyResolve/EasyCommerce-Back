package easycommerce.easycommerce.CodigoPostalZona.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.CodigoPostalZona.Model.CodigoPostalZona;

@Repository
public interface CodigoPostalZonaRepository extends JpaRepository<CodigoPostalZona,Long> {

}
