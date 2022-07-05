package restapi.webapp;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookInfoEntityAssembler implements RepresentationModelAssembler<BookInfo, EntityModel<BookInfo>> {

    @Override
    public EntityModel<BookInfo> toModel(BookInfo book) {
        return EntityModel.of(book,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(book.getBookID())).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back all books"));
    }

}
