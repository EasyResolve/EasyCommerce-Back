package easycommerce.easycommerce.Excepciones;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementExcepcion(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStockException(OutOfStockException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuotationNotFoundException.class)
    public ResponseEntity<String> handleQuotationNotFoundException(QuotationNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCouponCodeException.class)
    public ResponseEntity<String> handleInvalidCuponCodeException(InvalidCouponCodeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFinalPriceException.class)
    public ResponseEntity<String> handleNotFinalPriceException(NotFinalPriceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<String> handleUserAlreadyVerifiedException(UserAlreadyVerifiedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemDimensionsNotFoundException.class)
    public ResponseEntity<String> handlerItemDimensionsNotFoundException(ItemDimensionsNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPriceListSelectionException.class)
    public ResponseEntity<String> handlerInvalidPriceListSelectionException(InvalidPriceListSelectionException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoValidCustomerTypeSelectedException.class)
    public ResponseEntity<String> handlerNoValidCustomerTypeSelectedException(NoValidCustomerTypeSelectedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStateChangeException.class)
    public ResponseEntity<String> handlerInvalidStateChangeException(InvalidStateChangeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }






}
