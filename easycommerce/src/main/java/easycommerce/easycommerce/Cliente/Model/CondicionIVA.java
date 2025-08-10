package easycommerce.easycommerce.Cliente.Model;

public enum CondicionIVA {
    RESPONSABLEINSCRIPTO(1),
    CONSUMIDORFINAL(2),
    MONOTRIBUTISTA(3);

    private final int codigo;

    CondicionIVA(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static CondicionIVA fromCodigo(int codigo) {
        for (CondicionIVA metodo : CondicionIVA.values()) {
            if (metodo.getCodigo() == codigo) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
