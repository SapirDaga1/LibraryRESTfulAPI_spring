package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookInfoController {
    private BookInfoRepo bookInfoRepo;
    private BookEntityFactory bookEntityFactory;
    private BookDTOFactory bookDTOFactory;

    public BookInfoController(BookInfoRepo bookInfoRepo, BookEntityFactory bookEntityFactory, BookDTOFactory bookDTOFactory) {
        this.bookInfoRepo = bookInfoRepo;
        this.bookEntityFactory = bookEntityFactory;
        this.bookDTOFactory = bookDTOFactory;
    }

    // 3 GET methods and 1 of other CRUD methods.

//    @GetMapping("/books")
//    public CollectionModel<EntityModel<BookInfo>> getAllBooks(){
//        List<EntityModel<BookInfo>> books =bookInfoRepo.findAll()
//                .stream().map(bookEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(books,linkTo(methodOn(BookInfoController.class)
//                .getAllBooks()).withSelfRel());
//
//    }
    //TODO: fix the problem with this specific path
    @GetMapping("/books")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getAllBooks() {
        return ResponseEntity.ok(bookEntityFactory.toCollectionModel(bookInfoRepo.findAll()));
    }
    @GetMapping("/book/{id}")
    public EntityModel<BookInfo> getSpecificBook(@PathVariable Long id){
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(()->
                new BookNotFoundException(id));
        return EntityModel.of(bookInfo,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(id)).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back to all books"));
    }

    @GetMapping("/book/{id}/info")
    public ResponseEntity<EntityModel<BookDTO>> bookDetails(@PathVariable Long id){
        return bookInfoRepo.findById(id).map(BookDTO::new).map(bookDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



}
