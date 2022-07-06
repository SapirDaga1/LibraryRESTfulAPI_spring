package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts BooksOrder objects into EntityModel<BooksOrder> objects.
public class BooksOrderrEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<BooksOrderr> {
    public BooksOrderrEntityFactory() {
        super(BooksOrderrController.class);
    }
}
