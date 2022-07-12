package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.pojo.BookInfo;
import restapi.webapp.controllers.BookInfoController;

@Component
// HATEOAS factory that converts BookInfo objects into EntityModel<BookInfo> objects.
public class BookEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<BookInfo> {
    public BookEntityFactory() {
        super(BookInfoController.class);
    }
}
