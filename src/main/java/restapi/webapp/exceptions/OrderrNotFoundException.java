package restapi.webapp.exceptions;

public class OrderrNotFoundException extends RuntimeException{
    public OrderrNotFoundException(int numberOfOrder){
        super("There is no order corresponding to numberOfOrder = " + numberOfOrder);
    }
}
