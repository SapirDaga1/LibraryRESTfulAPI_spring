package restapi.webapp.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long numberOfOrder) {
        super("There is no order corresponding to numberOfOrder = " + numberOfOrder);
    }
}
