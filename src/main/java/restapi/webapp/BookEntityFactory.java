package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts BookInfo objects into EntityModel<BookInfo> objects.
public class BookEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<BookInfo> {
    public BookEntityFactory() {
        super(BookInfoController.class);
    }
}
