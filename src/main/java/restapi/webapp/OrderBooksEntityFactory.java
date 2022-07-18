package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts BooksOrder objects into EntityModel<BooksOrder> objects.
public class OrderBooksEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<OrderBooks> {
    public OrderBooksEntityFactory() {
        super(OrderBooksController.class);
    }
}
