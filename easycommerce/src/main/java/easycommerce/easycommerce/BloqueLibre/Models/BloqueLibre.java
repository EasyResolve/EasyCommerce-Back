package easycommerce.easycommerce.BloqueLibre.Models;

import jakarta.persistence.Entity;
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
public class BloqueLibre {
    private Long id;
    private TipoBloque tipoBloque;
    private TipoFiltro tipoFiltro;
    private String descripcion;
    private Integer orden;
    private String titulo;
}
