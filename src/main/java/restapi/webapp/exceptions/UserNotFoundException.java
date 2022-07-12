package restapi.webapp.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("There is no user corresponding to id = " + id);
    }
}
