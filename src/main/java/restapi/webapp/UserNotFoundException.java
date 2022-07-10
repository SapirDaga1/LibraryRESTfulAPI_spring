package restapi.webapp;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super("There is no user corresponding to email = " + email);
    }
}
