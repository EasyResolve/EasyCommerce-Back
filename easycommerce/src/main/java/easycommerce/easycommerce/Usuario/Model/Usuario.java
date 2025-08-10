package easycommerce.easycommerce.Usuario.Model;


import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.CodigoCambioPassword.Model.CodigoCambioPassword;
import easycommerce.easycommerce.CodigoVerificacion.Model.CodigoVerificacion;
import easycommerce.easycommerce.Rol.Model.Rol;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ManyToOne()
    @JoinColumn(name = "rolId", nullable = false)
    private Rol rol;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clienteId")
    private Cliente cliente;
    private boolean validado;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "codigoVerificacionId")
    private CodigoVerificacion codigoVerificacion;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "codigoCambioPasswordId")
    private CodigoCambioPassword codigoCambioPassword;

}
