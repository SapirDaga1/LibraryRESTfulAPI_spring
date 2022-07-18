package restapi.webapp;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @GetMapping("/order/cityDelivery/{city}")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByCityDelivery(@PathVariable("city") String city) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findByCityOfDelivery(city).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return  ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel()));

    }

    @GetMapping("/order/byPrice/{price}")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByPrice(@PathVariable("price") int price) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findByPrice(price).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return  ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel()));
    }

    @GetMapping("/order/byDate")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getUserByOrderDate(@RequestParam("date") String date) {

        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderrRepo.findByDateOfOrder(date).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return  ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel()));
    }

    @GetMapping("/order/{numberOfOrder}/info")
    public ResponseEntity<EntityModel<OrderBooks>> bookDetails(@PathVariable Long id){
        return booksOrderrRepo.findById(id).map(OrderBooks::new).map(booksOrderrEntityFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //returns always status code 200
    @GetMapping("/order/betweenPrices")
    public CollectionModel<EntityModel<OrderBooks>> getOrdersBetweenPrices(@RequestParam int fromPrice, @RequestParam int toPrice) {
        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(),false)
                .filter(order -> (order.getPrice() <= toPrice && order.getPrice() >= fromPrice))
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel());
    }

    //TODO: id with autogenerate shouldn't be entered by user.
    @PostMapping("/order/add")
    public ResponseEntity<EntityModel<OrderBooks>> addOrder(@RequestBody OrderBooks orderr){
        OrderBooks newOrder = booksOrderrRepo.save(orderr);
        return ResponseEntity.created(linkTo(methodOn(OrderBooksController.class)
                        .getSpecificOrder(newOrder.getId())).toUri())
                .body(booksOrderrEntityFactory.toModel(newOrder));
    }

    @GetMapping("/order/betweenDates")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersBetweenDates
            (@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
             @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception{

        //this is the format of the date we want to use(filter the date of book from database to this format)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(),false)
                .filter(book -> {
                    try {
                        return (format.parse(book.getDateOfOrder()).getTime()>= fromDate.getTime() &&
                                format.parse(book.getDateOfOrder()).getTime()<= toDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }return false;
                })
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(books,linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel()));
    }


    @GetMapping("/orders/numberOfBooks")
    public CollectionModel<EntityModel<OrderBooks>> getOrderWithMaxBooks(@RequestParam("numberOfBooks") int numberOfBooks) {
        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
                .filter(book -> book.getBooksList().size() <= numberOfBooks)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        return CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withSelfRel());
    }
    //TODO: Methods with complex segmentations
}

