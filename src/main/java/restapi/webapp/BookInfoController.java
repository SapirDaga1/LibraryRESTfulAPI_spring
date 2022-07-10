package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;


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

    /**
     *
     * @return an iterable of all books.
     */
    @GetMapping("/books")
    public Iterable<BookInfo>getAllBooks(){return bookInfoRepo.findAll();}
    //    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getAllBooks() {
//        return ResponseEntity.ok(bookEntityFactory.toCollectionModel(bookInfoRepo.findAll()));
//    }

    /**
     *
     * @param id of a specific book
     * @return link to specific book by it id and link to all the books
     */
    @GetMapping("/book/{id}")
    public EntityModel<BookInfo> getSpecificBook(@PathVariable Long id){
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(()->
                new BookNotFoundException(id));
        return EntityModel.of(bookInfo,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(id)).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back to all books"));
    }

    /**
     *
     * @param id of a specific book.
     * @return information/details about this book.
     */
    @GetMapping("/book/{id}/info")
    public ResponseEntity<EntityModel<BookDTO>> bookDetails(@PathVariable Long id){
        return bookInfoRepo.findById(id).map(BookDTO::new).map(bookDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //TODO: fix this method.
//    @GetMapping("/books")
//    @ResponseBody
//    public CollectionModel<EntityModel<BookInfo>> getBooksUnderPages(@RequestParam int pages){
//            List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
//                    .stream().filter(book -> book.getPageCount()<pages)
//                    .map(bookEntityFactory::toModel).collect(Collectors.toList());
//            return CollectionModel.of(books,linkTo(methodOn(BookInfoController.class)
//                    .getAllBooks()).withSelfRel());
//    }
    //TODO: fix this method.
//    @GetMapping("/book")
//    public ResponseEntity<EntityModel<BookDTO>> getBookByTitle(@RequestParam String title){
//        ResponseEntity<EntityModel<BookDTO>> book = bookInfoRepo.findByTitle(title);
//        return book;
//    }

    //TODO: add a message to request body
    /**
     *
     * @param id of specific book.
     * @return response of deleting the book.
     * @throws BookNotFoundException if book does not exist.
     */
    @DeleteMapping("/book/{id}")
    public Map<String, Boolean> deleteBook(@PathVariable(value = "id") Long id)
            throws BookNotFoundException {
        BookInfo book = bookInfoRepo.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookInfoRepo.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    //TODO: add 2 methods with 2 request param
    //get method with 2 request param - books between range of pages
    //get method with 2 request param - books which published between range of dates.

    //TODO: Methods with complex segmentations

}
