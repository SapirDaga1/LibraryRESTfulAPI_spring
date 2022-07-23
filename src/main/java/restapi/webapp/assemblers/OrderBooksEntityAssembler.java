package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.entities.OrderBooks;
import restapi.webapp.controllers.OrderBooksController;

@Component
// HATEOAS factory that converts BooksOrder objects into EntityModel<BooksOrder> objects.
public class OrderBooksEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<OrderBooks> {
    public OrderBooksEntityAssembler() {
        super(OrderBooksController.class);
    }
}
