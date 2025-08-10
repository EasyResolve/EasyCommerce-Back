package easycommerce.easycommerce.Pedido.Model;

public enum TipoEnvio {
    RETIROENLOCAL(1),
    ENVIOADOMICILIO(2);

    private final int codigo;

    TipoEnvio(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoEnvio fromCodigo(int codigo) {
        for (TipoEnvio metodo : TipoEnvio.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
