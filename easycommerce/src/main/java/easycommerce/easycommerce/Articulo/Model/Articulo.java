package easycommerce.easycommerce.Articulo.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import easycommerce.easycommerce.Cliente.Model.Cliente;
import easycommerce.easycommerce.Marca.Model.Marca;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMayorista;
import easycommerce.easycommerce.Parametros.Model.ListaPrecioMinorista;
import easycommerce.easycommerce.Rubro.Model.Rubro;
import easycommerce.easycommerce.SubRubro.Model.SubRubro;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Articulo {
    @Id
    private Long id;
    private String nombre;
    @Column(length = 50000)
    private String descripcion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rubroId")
    private Rubro rubro;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subRubroId")
    private SubRubro subRubro;
    private Integer stockActual;
    private BigDecimal precioOferta;
    private BigDecimal preVtaPub1;
    private BigDecimal preVtaPub2;
    private BigDecimal preVtaPub3;
    private BigDecimal preVtaMay1;
    private BigDecimal preVtaMay2;
    private BigDecimal preVtaMay3;
    private boolean esOferta;
    private boolean precioDolar;
    private String fechaDesdeOferta;
    private String fechaHastaOferta;
    private boolean esNuevo;
    private boolean activo;
    private Double porcentajeOferta;
    private String codigoOrigen;
    private String codigoDeBarra;
    @ElementCollection
    @CollectionTable(name = "articuloXImagenes", joinColumns = @JoinColumn(name = "articuloId"))
    @Column(name = "url")
    private List<String> urlImagenes;
    private Double alto;
    private Double ancho;
    private Double largo;
    private Double peso;
    private Double alicuotaIva;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "marcaId")
    private Marca marca;
    private boolean consultar;
    private boolean destacado;

    public Articulo(Articulo original) {
        this.id = original.id;
        this.nombre = original.nombre;
        this.descripcion = original.descripcion;
        this.rubro = original.rubro;
        this.subRubro = original.subRubro;
        this.stockActual = original.stockActual;
        this.preVtaPub1 = original.preVtaPub1;
        this.preVtaPub2 = original.preVtaPub2;
        this.preVtaPub3 = original.preVtaPub3;
        this.preVtaMay1 = original.preVtaMay1;
        this.preVtaMay2 = original.preVtaMay2;
        this.preVtaMay3 = original.preVtaMay3;
        this.precioOferta = original.precioOferta;
        this.esOferta = original.esOferta;
        this.precioDolar = original.precioDolar;
        this.fechaDesdeOferta = original.fechaDesdeOferta;
        this.fechaHastaOferta = original.fechaHastaOferta;
        this.esNuevo = original.esNuevo;
        this.activo = original.activo;
        this.porcentajeOferta = original.porcentajeOferta;
        this.codigoOrigen = original.codigoOrigen;
        this.codigoDeBarra = original.codigoDeBarra;
        this.urlImagenes = original.urlImagenes != null ? new ArrayList<>(original.urlImagenes) : null;
        this.alto = original.alto;
        this.ancho = original.ancho;
        this.largo = original.largo;
        this.peso = original.peso;
        this.alicuotaIva = original.alicuotaIva;
        this.marca = original.marca;
    }


    public Double obtenerVolumen(){
        Double volumen = alto * ancho * largo;
        return volumen;
    } 

    public void calcularPrecios(Cliente cliente, 
                              BigDecimal cotizacionDolar,
                              ListaPrecioMayorista listaMayorista,  ListaPrecioMinorista listaMinorista) throws Exception {
        
        // 1. Conversión a pesos si está en dólares
        if (this.precioDolar) {
            convertirPreciosADolar(cotizacionDolar);
        }

        // 2. Procesamiento de ofertas
        procesarOferta(cliente, listaMayorista, listaMinorista);
    }

    // Métodos auxiliares privados
    private void convertirPreciosADolar(BigDecimal cotizacion) {
        this.preVtaPub1 = convertirPrecio(this.preVtaPub1, cotizacion);
        this.preVtaPub2 = convertirPrecio(this.preVtaPub2, cotizacion);
        this.preVtaPub3 = convertirPrecio(this.preVtaPub3, cotizacion);
        this.preVtaMay1 = convertirPrecio(this.preVtaMay1, cotizacion);
        this.preVtaMay2 = convertirPrecio(this.preVtaMay2, cotizacion);
        this.preVtaMay3 = convertirPrecio(this.preVtaMay3, cotizacion);

        if (this.precioOferta != null) {
            this.precioOferta = convertirPrecio(this.precioOferta, cotizacion);
        }
    }

    private BigDecimal convertirPrecio(BigDecimal precio, BigDecimal cotizacion) {
        return precio.multiply(cotizacion).setScale(2, RoundingMode.HALF_UP);
    }

    private void procesarOferta(Cliente cliente, ListaPrecioMayorista listaMayorista ,ListaPrecioMinorista listaMinorista) throws Exception {
        if (!this.esOferta) return;

        LocalDate hoy = LocalDate.now();
        LocalDate fechaDesde = parseFecha(this.fechaDesdeOferta);
        LocalDate fechaHasta = parseFecha(this.fechaHastaOferta);

        // Validar vigencia de la oferta
        if (!validarVigenciaOferta(hoy, fechaDesde, fechaHasta)) {
            resetearOferta();
            return;
        }
        
        /*if(this.precioOferta.compareTo(BigDecimal.ZERO) >= 0 && this.porcentajeOferta != null && this.porcentajeOferta > 0){
            resetearOferta(); 
            return;
        }
        */
        
        // Si ya tiene precio de oferta directo, no hacer nada más
        if (this.precioOferta != null && this.precioOferta.compareTo(BigDecimal.ONE) > 0) {
            return;
        }

        // Calcular oferta según porcentaje
        if (this.porcentajeOferta != null && this.porcentajeOferta > 0) {
            BigDecimal precioBase = obtenerPrecioBase(cliente, listaMayorista, listaMinorista);
            BigDecimal descuento = precioBase.multiply(BigDecimal.valueOf(this.porcentajeOferta / 100));
            this.precioOferta = precioBase.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private boolean validarVigenciaOferta(LocalDate hoy, LocalDate fechaDesde, LocalDate fechaHasta) {
        return fechaDesde != null && 
               !hoy.isBefore(fechaDesde) && 
               (fechaHasta == null || !hoy.isAfter(fechaHasta));
    }

    private void resetearOferta() {
        this.esOferta = false;
        this.fechaDesdeOferta = null;
        this.fechaHastaOferta = null;
        this.porcentajeOferta = 0.0;
        this.precioOferta = BigDecimal.ZERO;
    }

    private LocalDate parseFecha(String fecha) {
        if (fecha == null || "null".equalsIgnoreCase(fecha)) return null;
        return LocalDate.parse(fecha);
    }

    private BigDecimal obtenerPrecioBase(Cliente cliente,ListaPrecioMayorista listaMayorista,  ListaPrecioMinorista listaMinorista) throws Exception {
        String tipoCliente = (cliente == null) ? "MINORISTA" : cliente.getTipoCliente().name();
        //si me viene un cliente primero veo que tenga lista, si no tiene lista, le asigno la lista por defecto de su tipo de cliente
        String listaNum = (cliente != null && cliente.getLista() != null && cliente.getLista() != 0) ? "LISTA" + cliente.getLista().toString() 
        :   (cliente != null && tipoCliente.equals("MAYORISTA"))  ?  listaMayorista.name()  :listaMinorista.name();

        String listaSeleccionada = tipoCliente.equals("MAYORISTA") ?
            "MAY" + listaNum :
            "PUB" + listaNum;

        return obtenerPrecioPorLista(listaSeleccionada);
    }

    private BigDecimal obtenerPrecioPorLista(String tipoLista) {
        switch (tipoLista.toUpperCase()) {
            case "PUBLISTA1": return this.preVtaPub1;
            case "PUBLISTA2": return this.preVtaPub2;
            case "PUBLISTA3": return this.preVtaPub3;
            case "MAYLISTA1": return this.preVtaMay1;
            case "MAYLISTA2": return this.preVtaMay2;
            case "MAYLISTA3": return this.preVtaMay3;
            default: throw new IllegalArgumentException("Tipo de lista inválido: " + tipoLista);
        }
    }

    // Método para obtener el precio final a mostrar
    public BigDecimal obtenerPrecioFinal(Cliente cliente, ListaPrecioMayorista listaMayorista,  ListaPrecioMinorista listaMinorista) throws Exception {
        //terrible lo que hice hacer aca 100% mi culpa
        String tipoCliente = (cliente == null) ? "MINORISTA" : cliente.getTipoCliente().name();
        if (this.precioOferta != null && this.precioOferta.compareTo(BigDecimal.ZERO) >= 0 && (!tipoCliente.equals("MAYORISTA"))) {
            BigDecimal precioFinal = this.precioOferta;
            BigDecimal precioBase = obtenerPrecioBase(cliente, listaMayorista, listaMinorista);
            BigDecimal mayor = precioFinal.compareTo(precioBase) >= 0 ? precioFinal : precioBase;
            if(mayor.equals(precioFinal)) {
                this.precioOferta = precioBase;
                return precioFinal;
            }
            else{
                this.precioOferta = precioFinal;
                return precioBase;
            }

        }
        else{
            resetearOferta();
        }
        return obtenerPrecioBase(cliente, listaMayorista, listaMinorista);
    }
}
    


