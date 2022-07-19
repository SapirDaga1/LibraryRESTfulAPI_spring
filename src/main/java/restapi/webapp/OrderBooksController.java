package restapi.webapp;

import io.swagger.v3.oas.annotations.Operation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
     * @return an Iterable of orders.
     */
    @GetMapping("/orders")
    @Operation(summary = "Get all orders.")
    public Iterable<OrderBooks> getAllOrders() {
        return booksOrderrRepo.findAll();
    }

    /**
     * @param id of specific order.
     * @return information/details about this order.
     */
    @GetMapping("/order/{id}")
    @Operation(summary = "Get a specific order by id.")
    public EntityModel<OrderBooks> getSpecificOrder(@PathVariable Long id) {
        OrderBooks booksOrderr = booksOrderrRepo.findById(id).orElseThrow(() ->
                new OrderNotFoundException(id));
        return EntityModel.of(booksOrderr,
                linkTo(methodOn(OrderBooksController.class).getSpecificOrder(id)).withSelfRel(),
                linkTo(methodOn(OrderBooksController.class).getAllOrders()).withRel("back to all orders"));
    }

    @GetMapping("/order/cityDelivery/{city}")
    @Operation(summary = "Get all orders to specific city to delivery.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByCityDelivery(@PathVariable("city") String city) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findByCityOfDelivery(city).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/order/byPrice/{price}")
    @Operation(summary = "Get all orders with exact price to pay.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByPrice(@PathVariable("price") int price) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findByPrice(price).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/order/byDate")
    @Operation(summary = "Get all orders that done by specific date.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getUserByOrderDate(@RequestParam("date") String date) {

        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderrRepo.findByDateOfOrder(date).spliterator(), false)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (orders.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/order/{id}/links")
    @Operation(summary = "Get a specific order by id.")
    public ResponseEntity<EntityModel<OrderBooks>> bookDetails(@PathVariable Long id) {
        return booksOrderrRepo.findById(id).map(OrderBooks::new).map(booksOrderrEntityFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/betweenPrices")
    @Operation(summary = "Get all orders in range of price to pay.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersBetweenPrices(@RequestParam int fromPrice, @RequestParam int toPrice) {
        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
                .filter(order -> (order.getPrice() <= toPrice && order.getPrice() >= fromPrice))
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (orders.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //TODO: id with autogenerate shouldn't be entered by user.
    @PostMapping("/order/add")
    @Operation(summary = "Add a new order.")
    public ResponseEntity<EntityModel<OrderBooks>> addOrder(@RequestBody OrderBooks orderr) {
        OrderBooks newOrder = booksOrderrRepo.save(orderr);
        return ResponseEntity.created(linkTo(methodOn(OrderBooksController.class)
                        .getSpecificOrder(newOrder.getId())).toUri())
                .body(booksOrderrEntityFactory.toModel(newOrder));
    }

    @GetMapping("/order/betweenDates")
    @Operation(summary = "Get all orders in range of dates.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersBetweenDates
            (@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
             @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception {

        //this is the format of the date we want to use(filter the date of order)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
                .filter(book -> {
                    try {
                        return (format.parse(book.getDateOfOrder()).getTime() >= fromDate.getTime() &&
                                format.parse(book.getDateOfOrder()).getTime() <= toDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/orders/numberOfBooks")
    @Operation(summary = "Get all books with up to number of books list.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrderWithMaxBooks(@RequestParam("numberOfBooks") int numberOfBooks) {
        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false)
                .filter(book -> book.getBooksList().size() <= numberOfBooks)
                .map(booksOrderrEntityFactory::toModel).collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //TODO: Methods with complex segmentations
    @GetMapping("/orders/maxDeliveryCity")
    @Operation(summary = "Get the city with the most deliveries.")
    public String getCityWithMaxDelivery() {
        Map<String, Integer> cityCount = new HashMap<>();
        List<OrderBooks> list = StreamSupport.stream(booksOrderrRepo.findAll().spliterator(), false).collect(Collectors.toList());
        //handling with counter...
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.contains(list.get(i).getCityOfDelivery())){
                System.out.println(cityCount.get(list.get(i).getCityOfDelivery())==null);
                if (cityCount.get(list.get(i).getCityOfDelivery())==null)
                    count=0;
                count++;
                cityCount.put(list.get(i).getCityOfDelivery(),count);
            }
        }
        int maxValue = (Collections.max(cityCount.values()));
        for (Map.Entry<String, Integer> map : cityCount.entrySet()) {
            if (map.getValue() == maxValue)
                return map.getKey();
        }
         return "none";
    }
}
