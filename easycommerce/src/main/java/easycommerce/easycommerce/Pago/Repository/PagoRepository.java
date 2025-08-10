package easycommerce.easycommerce.Pago.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Pago.Model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago,Long>{


    
}
