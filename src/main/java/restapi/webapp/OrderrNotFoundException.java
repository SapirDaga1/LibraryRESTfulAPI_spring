package restapi.webapp;

public class OrderrNotFoundException extends RuntimeException{
    public OrderrNotFoundException(Long numberOfOrder){
        super("There is no order corresponding to numberOfOrder = " + numberOfOrder);
    }
}
