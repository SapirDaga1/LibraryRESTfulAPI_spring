package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts BookInfo objects into EntityModel<BookInfo> objects.
public class BookEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<BookInfo> {
    public BookEntityAssembler() {
        super(BookInfoController.class);
    }
}
