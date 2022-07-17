package restapi.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderBooksController {

    @Autowired
    private OrderBooksRepo booksOrderrRepo;
    private OrderBooksEntityFactory booksOrderrEntityFactory;

    public OrderBooksController(OrderBooksRepo booksOrderrRepo, OrderBooksEntityFactory booksOrderrEntityFactory) {
        this.booksOrderrRepo = booksOrderrRepo;
        this.booksOrderrEntityFactory = booksOrderrEntityFactory;
    }

    //TODO: 3 GET methods and 1 of other CRUD methods.

    /**
     *
     * @return an Iterable of orders.
     */
    @GetMapping("/orders")
    public Iterable<OrderBooks>getAllOrders(){ return booksOrderrRepo.findAll(); }

    /**
     *
     * @param id of specific order.
     * @return information/details about this order.
     */
    @GetMapping("/order/{id}")
    public EntityModel<OrderBooks> getSpecificOrder(@PathVariable Long id){
        OrderBooks booksOrderr = booksOrderrRepo.findById(id).orElseThrow(()->
                new OrderNotFoundException(id));
        return EntityModel.of(booksOrderr,
                linkTo(methodOn(OrderBooksController.class).getSpecificOrder(id)).withSelfRel(),
                linkTo(methodOn(OrderBooksController.class).getAllOrders()).withRel("back to all orders"));
    }
    //TODO - fix the error response status is 500.
    @GetMapping("/order/cityDelivery")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByCityDelivery(@RequestParam("city") String city) {
        return ResponseEntity.ok(
                booksOrderrEntityFactory.toCollectionModel(booksOrderrRepo.findByCityOfDelivery(city)));
    }

    //TODO - fix the error response status is 500
    @GetMapping("/order/byDate/{date}")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getUserByOrderDate(@PathVariable("date") String date) {
        return ResponseEntity.ok(
                booksOrderrEntityFactory.toCollectionModel(booksOrderrRepo.findByDateOfOrderr(date)));
    }

//    @GetMapping("/order/{numberOfOrder}/info")
//    public ResponseEntity<EntityModel<BooksOrderr>> bookDetails(@PathVariable Long id){
//        return booksOrderrRepo.findById(id).map(BooksOrderr::new).map(booksOrderrEntityFactory::toModel)
//                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }

    //TODO: id with autogenerate shouldn't be entered by user.
    @PostMapping("/order/add")
    public ResponseEntity<EntityModel<OrderBooks>> addOrder(@RequestBody OrderBooks orderr){
        OrderBooks newOrder = booksOrderrRepo.save(orderr);
        return ResponseEntity.created(linkTo(methodOn(OrderBooksController.class)
                        .getSpecificOrder(newOrder.getNumberOfOrderr())).toUri())
                .body(booksOrderrEntityFactory.toModel(newOrder));
    }


//    @GetMapping("/orders/numberOfBooks")
//    public CollectionModel<EntityModel<BooksOrderr>> getOrderWithMaxBooks(@RequestParam("numberOfBooks") int numberOfBooks) {
//        List<EntityModel<BooksOrderr>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
//                .map(BooksOrderr::new).filter(book -> book.size() <= numberOfBooks)
//                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
//                .getAllBooks()).withSelfRel());
//    }
    //TODO: add 2 methods with 2 request param

    //add get orders between dates(2 request params)
    //add get orders between size of order (booksList)
    //TODO: Methods with complex segmentations
}

