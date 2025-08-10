package easycommerce.easycommerce.Rubro.Model;

import java.util.List;

import easycommerce.easycommerce.SubRubro.Model.SubRubro;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
public class Rubro {
    @Id
    private Long codigo;
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subRubroId")
    private List<SubRubro> subRubros;

    public void agregarSubRubro(SubRubro subRubro){
        subRubros.add(subRubro);
    }
}
