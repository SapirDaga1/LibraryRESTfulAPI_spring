package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts BooksOrder objects into EntityModel<BooksOrder> objects.
public class OrderBooksEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<OrderBooks> {
    public OrderBooksEntityAssembler() {
        super(OrderBooksController.class);
    }
}
