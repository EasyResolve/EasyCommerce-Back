package easycommerce.easycommerce.Cliente.Model;

public enum TipoCliente {
    MINORISTA(1),
    MAYORISTA(2);

    private final int codigo;

    TipoCliente(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoCliente fromCodigo(int codigo) {
        for (TipoCliente metodo : TipoCliente.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
