package restapi.webapp;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long numberOfOrder) {
        super("There is no order corresponding to numberOfOrder = " + numberOfOrder);
    }
}
