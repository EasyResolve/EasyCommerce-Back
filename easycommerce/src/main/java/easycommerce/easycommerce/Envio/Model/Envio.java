package easycommerce.easycommerce.Envio.Model;

import java.math.BigDecimal;
import java.util.List;

import easycommerce.easycommerce.Articulo.Model.Articulo;
import easycommerce.easycommerce.Direccion.Model.Direccion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "articulosXenvio", joinColumns = @JoinColumn(name = "envioId"), inverseJoinColumns = @JoinColumn(name = "articuloId"))
    private List<Articulo> articulos;
    @ManyToOne
    @JoinColumn(name = "direccionId")
    private Direccion direccion;
    private String codigoSeguimiento;
    private BigDecimal costoEnvio;
}
