package easycommerce.easycommerce.Excepciones;

public class UserAlreadyVerifiedException extends Exception {
    public UserAlreadyVerifiedException(String message) {
        super(message);
    }
}
