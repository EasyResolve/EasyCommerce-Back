package easycommerce.easycommerce.Caja.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Double largo;
    private Double ancho;
    private Double alto;
    private Double peso;
    @Column(nullable = true)
    @Nullable
    private Integer stock;

    public Double calcularVolumen(){
        Double volumen = largo*ancho*alto;
        return volumen;
    }
}
