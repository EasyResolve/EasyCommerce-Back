package easycommerce.easycommerce.BloqueLibre.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.BloqueLibre.Models.BloqueLibre;

@Repository
public interface BloqueLibreRepository extends JpaRepository<BloqueLibre, Long> {

}
