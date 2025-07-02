package exceptions;

public class ProductExistsException extends Exception{
    public ProductExistsException(String message){
        super(message);
    }
}