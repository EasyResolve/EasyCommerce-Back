package easycommerce.easycommerce.Pago.Model;

public enum TipoPago {
    EFECTIVO(1),
    TRANSFERENCIA(2),
    NAVE(3);

    private final int codigo;

    TipoPago(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoPago fromCodigo(int codigo) {
        for (TipoPago metodo : TipoPago.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
