package restapi.webapp;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("There is no user corresponding to id = " + id);
    }
}
