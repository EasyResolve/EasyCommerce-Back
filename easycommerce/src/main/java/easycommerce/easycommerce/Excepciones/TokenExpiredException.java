package easycommerce.easycommerce.Excepciones;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String message) {
        super(message);
    }
}
