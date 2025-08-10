package easycommerce.easycommerce.Excepciones;

public class EntityAlreadyExistsException extends Exception {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
