package easycommerce.easycommerce.Parametros.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Parametro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private String valor;

    public void setListaPrecioMayorista(ListaPrecioMayorista listaPrecioMayorista){
        this.valor = listaPrecioMayorista.name();
    }
    @JsonIgnore
    public ListaPrecioMayorista getListaPrecioMayorista(){
        return ListaPrecioMayorista.valueOf(this.valor);
    }

    public void setListaPrecioMinorista(ListaPrecioMinorista listaPrecioMinorista){
        this.valor = listaPrecioMinorista.name();
    }
    @JsonIgnore
    public ListaPrecioMinorista getListaPrecioMinorista(){
        return ListaPrecioMinorista.valueOf(this.valor);
    }


}
