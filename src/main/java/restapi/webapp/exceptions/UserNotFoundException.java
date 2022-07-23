package restapi.webapp.exceptions;

import java.util.Optional;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("There is no user corresponding to id = " + id);
    }

    public UserNotFoundException(String email) {
        super("There is no user corresponding to email = " + email);
    }

    public UserNotFoundException(Optional<String> lastName) {
        super("There is no user corresponding to last name = " + lastName);
    }
}
