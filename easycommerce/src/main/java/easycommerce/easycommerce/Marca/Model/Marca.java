package easycommerce.easycommerce.Marca.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Marca {
    @Id
    private Long codigo;
    private String descripcion;
    private String urlImagen;
    private boolean mostrar;
}
