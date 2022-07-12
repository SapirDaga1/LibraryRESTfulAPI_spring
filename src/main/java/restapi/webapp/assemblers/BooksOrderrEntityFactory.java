package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.pojo.BooksOrderr;
import restapi.webapp.controllers.BooksOrderrController;

@Component
// HATEOAS factory that converts BooksOrder objects into EntityModel<BooksOrder> objects.
public class BooksOrderrEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<BooksOrderr> {
    public BooksOrderrEntityFactory() {
        super(BooksOrderrController.class);
    }
}
