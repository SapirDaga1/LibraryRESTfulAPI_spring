package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.entities.BookInfo;
import restapi.webapp.controllers.BookInfoController;

@Component
// HATEOAS factory that converts BookInfo objects into EntityModel<BookInfo> objects.
public class BookEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<BookInfo> {
    public BookEntityAssembler() {
        super(BookInfoController.class);
    }
}
