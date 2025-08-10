package easycommerce.easycommerce.Cliente.Model;

import java.util.List;

import easycommerce.easycommerce.Direccion.Model.Direccion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String documento;
    private String razonSocial;
    private String nombre;
    private String apellido;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private CondicionIVA condicionIva;
    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;
    private Integer lista;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "clienteId")
    List<Direccion> direcciones;
}
