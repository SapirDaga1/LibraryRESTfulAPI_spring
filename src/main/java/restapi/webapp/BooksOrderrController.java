package restapi.webapp;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BooksOrderrController {
    private BooksOrderrRepo booksOrderrRepo;
    private BooksOrderrEntityFactory booksOrderrEntityFactory;

    public BooksOrderrController(BooksOrderrRepo booksOrderrRepo, BooksOrderrEntityFactory booksOrderrEntityFactory) {
        this.booksOrderrRepo = booksOrderrRepo;
        this.booksOrderrEntityFactory = booksOrderrEntityFactory;
    }

    //TODO: 3 GET methods and 1 of other CRUD methods.

    //TODO: fix problem of null parameters in the booksList.
    @GetMapping("/orders")
    public Iterable<BooksOrderr>getAllOrders(){return booksOrderrRepo.findAll();}

    @GetMapping("/order/{numberOfOrder}")
    public EntityModel<BooksOrderr> getSpecificOrder(@PathVariable Long id){
        BooksOrderr booksOrderr = booksOrderrRepo.findById(id).orElseThrow(()->
                new BookNotFoundException(id));
        return EntityModel.of(booksOrderr,
                linkTo(methodOn(BooksOrderrController.class).getSpecificOrder(id)).withSelfRel(),
                linkTo(methodOn(BooksOrderrController.class).getAllOrders()).withRel("back to all orders"));
    }

//    @GetMapping("/order/{numberOfOrder}/info")
//    public ResponseEntity<EntityModel<BooksOrderr>> bookDetails(@PathVariable Long id){
//        return booksOrderrRepo.findById(id).map(BooksOrderr::new).map(booksOrderrEntityFactory::toModel)
//                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }

//    @PostMapping("/newOrder")
//    public BooksOrderr newOrder(@Valid @RequestBody BookDTO book){
//        return BooksOrderrRepo.save(book);}

//    @PostMapping("/books")
//    public BooksOrderr addNewBook (@Valid @RequestBody BookDTO newBook){ // Relied on Jackson component for serialization
//        return booksOrderrRepo.save(newBook); // Relied on Jackson component for deserialization
//    }
    //TODO: add 2 methods with 2 request param

    //TODO: Methods with complex segmentations
}

