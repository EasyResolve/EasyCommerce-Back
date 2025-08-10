package easycommerce.easycommerce.Parametros.Model;

public enum ListaPrecioMayorista {
    Lista1(1),
    Lista2(2),
    Lista3(3);

    private final int codigo;

    ListaPrecioMayorista(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static ListaPrecioMayorista fromCodigo(int codigo) {
        for (ListaPrecioMayorista metodo : ListaPrecioMayorista.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
