package easycommerce.easycommerce.Direccion.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import easycommerce.easycommerce.Direccion.Model.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion,Long>{

}
