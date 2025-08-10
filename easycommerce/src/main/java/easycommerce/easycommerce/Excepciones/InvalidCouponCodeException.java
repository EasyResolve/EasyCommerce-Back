package easycommerce.easycommerce.Excepciones;

public class InvalidCouponCodeException extends Exception {
    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
