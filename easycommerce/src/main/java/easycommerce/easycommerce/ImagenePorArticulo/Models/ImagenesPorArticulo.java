package easycommerce.easycommerce.ImagenePorArticulo.Models;

import easycommerce.easycommerce.Articulo.Model.Articulo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ImagenesPorArticuloTemporal")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagenesPorArticulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "articuloId")
    private Articulo articulo;
    private String nombreImagen;
}
