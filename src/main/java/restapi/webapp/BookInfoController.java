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
    private BookInfoEntityAssembler bookInfoEntityAssembler;

    public BookInfoController(BookInfoRepo bookInfoRepo, BookInfoEntityAssembler bookInfoEntityAssembler) {
        this.bookInfoRepo = bookInfoRepo;
        this.bookInfoEntityAssembler = bookInfoEntityAssembler;
    }

    // 3 GET methods and 1 of other CRUD methods.

    @GetMapping("/books")
    CollectionModel<EntityModel<BookInfo>> getAllBooks(){
        List<EntityModel<BookInfo>> books =bookInfoRepo.findAll()
                .stream().map(bookInfoEntityAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(books,linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel());

    }
    @GetMapping("/Books/{id}")
    EntityModel<BookInfo> getSpecificBook(@PathVariable Long id){
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(()->
                new BookNotFoundException(id));
        return EntityModel.of(bookInfo,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(id)).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back to all books"));
    }

//    @GetMapping("/Books/{id}/info")
//    ResponseEntity<EntityModel<BookInfo>> bookDetails(@PathVariable Long id){
//        return bookInfoRepo.findById(id).map(BookInfo::new).map(BookInfoEntityAssembler::toModel)
//                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }


}
