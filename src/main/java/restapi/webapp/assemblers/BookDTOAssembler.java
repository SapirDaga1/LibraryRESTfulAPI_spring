package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.dto.BookDTO;
import restapi.webapp.controllers.BookInfoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class BookDTOAssembler implements SimpleRepresentationModelAssembler<BookDTO> {
    /**
     * Define links to add to every individual {@link EntityModel}.
     *
     * @param resource
     */
    @Override
    public void addLinks(EntityModel<BookDTO> resource) {
        resource.add(linkTo(methodOn(BookInfoController.class).getSpecificBook(resource.getContent().getID())).withRel("single book"));
        resource.add(linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("all books information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BookDTO>> resources) {
        resources.add(linkTo(methodOn(BookInfoController.class).getAllBooks()).withSelfRel());
    }

    @Override
    public EntityModel<BookDTO> toModel(BookDTO book) {
        return EntityModel.of(book,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(book.getID())).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back all books"));
    }
}
