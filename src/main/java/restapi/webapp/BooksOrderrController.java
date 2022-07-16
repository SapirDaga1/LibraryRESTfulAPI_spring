package restapi.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BooksOrderrController {

    @Autowired
    private BooksOrderrRepo booksOrderrRepo;
    private BooksOrderrEntityFactory booksOrderrEntityFactory;

    public BooksOrderrController(BooksOrderrRepo booksOrderrRepo, BooksOrderrEntityFactory booksOrderrEntityFactory) {
        this.booksOrderrRepo = booksOrderrRepo;
        this.booksOrderrEntityFactory = booksOrderrEntityFactory;
    }

    //TODO: 3 GET methods and 1 of other CRUD methods.

    @GetMapping("/orders")
    public Iterable<BooksOrderr>getAllOrders(){return booksOrderrRepo.findAll();}

    @GetMapping("/order/{id}")
    public EntityModel<BooksOrderr> getSpecificOrder(@PathVariable Long id){
        BooksOrderr booksOrderr = booksOrderrRepo.findById(id).orElseThrow(()->
                new OrderrNotFoundException(id));
        return EntityModel.of(booksOrderr,
                linkTo(methodOn(BooksOrderrController.class).getSpecificOrder(id)).withSelfRel(),
                linkTo(methodOn(BooksOrderrController.class).getAllOrders()).withRel("back to all orders"));
    }

//    @GetMapping("/order/{numberOfOrder}/info")
//    public ResponseEntity<EntityModel<BooksOrderr>> bookDetails(@PathVariable Long id){
//        return booksOrderrRepo.findById(id).map(BooksOrderr::new).map(booksOrderrEntityFactory::toModel)
//                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }

    //TODO:fix this method- bad requset 400
//    @PostMapping("/newOrder")
//    public  ResponseEntity<EntityModel<BooksOrderr>> newOrder(@RequestBody BooksOrderr orderr){
//        BooksOrderr savedBooksOrderr = booksOrderrRepo.save(orderr);
//        return ResponseEntity.created(linkTo(methodOn(BooksOrderrController.class)
//                .getSpecificOrder(savedBooksOrderr.getNumberOfOrderr())).toUri())
//                .body(booksOrderrEntityFactory.toModel(savedBooksOrderr));}

    //TODO: id with autogenerate shouldn't be entered by user.
    @PostMapping("/order/add")
    public ResponseEntity<EntityModel<BooksOrderr>> addOrderr(@RequestBody BooksOrderr orderr){
        BooksOrderr newOrderr = booksOrderrRepo.save(orderr);
        return ResponseEntity.created(linkTo(methodOn(BooksOrderrController.class)
                        .getSpecificOrder(newOrderr.getNumberOfOrderr())).toUri())
                .body(booksOrderrEntityFactory.toModel(newOrderr));
    }

//    @GetMapping("/orders/numberOfBooks")
//    public CollectionModel<EntityModel<BooksOrderr>> getOrderWithMaxBooks(@RequestParam("numberOfBooks") int numberOfBooks) {
//        List<EntityModel<BooksOrderr>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
//                .filter(book -> book.getBooksList().size() <= numberOfBooks)
//                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
//                .getAllBooks()).withSelfRel());
//    }
    //TODO: add 2 methods with 2 request param

    //TODO: Methods with complex segmentations
}

