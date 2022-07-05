package restapi.webapp;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id){
        super("There is not book corresponding to id =" + id);
    }
}
