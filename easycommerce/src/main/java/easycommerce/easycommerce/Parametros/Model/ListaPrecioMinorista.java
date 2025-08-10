package easycommerce.easycommerce.Parametros.Model;

public enum ListaPrecioMinorista {
    Lista1(1),
    Lista2(2),
    Lista3(3);

    private final int codigo;

    ListaPrecioMinorista(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static ListaPrecioMinorista fromCodigo(int codigo) {
        for (ListaPrecioMinorista metodo : ListaPrecioMinorista.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
