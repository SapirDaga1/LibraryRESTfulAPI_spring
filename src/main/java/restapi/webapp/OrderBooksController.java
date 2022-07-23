package restapi.webapp;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderBooksController {

    @Autowired
    private OrderBooksRepo booksOrderRepo;
    private OrderBooksEntityAssembler orderBooksEntityAssembler;

    public OrderBooksController(OrderBooksRepo booksOrderRepo, OrderBooksEntityAssembler orderBooksEntityAssembler) {
        this.booksOrderRepo = booksOrderRepo;
        this.orderBooksEntityAssembler = orderBooksEntityAssembler;
    }


    /**
     * This method search for all orders from database.
     *
     * @return an Iterable of orders.
     */
    @GetMapping("/orders")
    @Operation(summary = "Get all orders.")
    public Iterable<OrderBooks> getAllOrders() {
        return booksOrderRepo.findAll();
    }

    /**
     * This method search for a specific order from database by its id.
     *
     * @param id of specific order.
     * @return EntityModel of OrderBooks.
     */
    @GetMapping("/order/{id}")
    @Operation(summary = "Get a specific order by id.")
    public EntityModel<OrderBooks> getSpecificOrder(@PathVariable Long id) {
        OrderBooks booksOrder = booksOrderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return EntityModel.of(booksOrder, linkTo(methodOn(OrderBooksController.class)
                .getSpecificOrder(id)).withSelfRel(), linkTo(methodOn(OrderBooksController.class)
                .getAllOrders()).withRel("back to all orders"));
    }

    /**
     * This method search for all orders with specific city to delivery.
     *
     * @param city represent city to delivery of order.
     * @return CollectionModel of EntityModels of OrderBooks.
     */
    @GetMapping("/order/city-delivery/{city}")
    @Operation(summary = "Get all orders with specific city to delivery.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByCityDelivery(@PathVariable("city") String city) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderRepo.findByCityOfDelivery(city).spliterator(), false)
                .map(orderBooksEntityAssembler::toModel)
                .collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * This method search for all orders with exact price of order.
     *
     * @param price represent price to pay of order.
     * @return CollectionModel of EntityModels of OrderBooks.
     */
    @GetMapping("/order/price/{price}")
    @Operation(summary = "Get all orders with exact price to pay.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getByPrice(@PathVariable("price") int price) {

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderRepo.findByPrice(price).spliterator(), false)
                .map(orderBooksEntityAssembler::toModel)
                .collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class)
                    .getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for all orders that was done by specific date.
     *
     * @param date represent date of order.
     * @return CollectionModel of EntityModels of OrderBooks.
     */
    @GetMapping("/order/date")
    @Operation(summary = "Get all orders that was done by specific date.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersByOrderDate(@RequestParam("date") String date) {

        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderRepo.findByDateOfOrder(date).spliterator(), false)
                .map(orderBooksEntityAssembler::toModel)
                .collect(Collectors.toList());
        if (orders.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class).getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method gives us links of a specific order by its id.
     *
     * @param id of a specific order.
     * @return EntityModel of OrderBooks.
     */
    @GetMapping("/order/{id}/links")
    @Operation(summary = "Get a specific order by id.")
    public ResponseEntity<EntityModel<OrderBooks>> getOrderLinks(@PathVariable Long id) {
        return booksOrderRepo.findById(id).map(OrderBooks::new).map(orderBooksEntityAssembler::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * This method search for all orders between range of prices.
     *
     * @param fromPrice represents a lower threshold of prices.
     * @param toPrice   represents an upper threshold of prices.
     * @return CollectionModel of EntityModels of OrderBooks.
     */
    @GetMapping("/order/between-prices")
    @Operation(summary = "Get all orders in range of price to pay.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersBetweenPrices(@RequestParam int fromPrice, @RequestParam int toPrice) {
        List<EntityModel<OrderBooks>> orders = StreamSupport.stream(booksOrderRepo.findAll().spliterator(), false)
                .filter(order -> (order.getPrice() <= toPrice && order.getPrice() >= fromPrice))
                .map(orderBooksEntityAssembler::toModel)
                .collect(Collectors.toList());
        if (orders.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderBooksController.class).getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method add a new order and save it in database.
     *
     * @param order represent a new order.
     * @return EntityModel of OrderBooks.
     */
    @PostMapping("/order/add")
    @Operation(summary = "Add a new order.")
    public ResponseEntity<EntityModel<OrderBooks>> addOrder(@Valid @RequestBody OrderBooks order) {

        OrderBooks newOrder = booksOrderRepo.save(order);
        return ResponseEntity.created(linkTo(methodOn(OrderBooksController.class)
                .getSpecificOrder(newOrder.getId())).toUri())
                .body(orderBooksEntityAssembler.toModel(newOrder));
    }

    /**
     * This method search for all orders that was done between range of dates.
     *
     * @param fromDate represents a lower threshold of dates.
     * @param toDate   represents an upper threshold of dates.
     * @return CollectionModel of EntityModels of OrderBooks.
     * @throws Exception
     */
    @GetMapping("/order/between-dates")
    @Operation(summary = "Get all orders in range of dates.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrdersBetweenDates(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception {

        //this is the format of the date we want to use(filter the date of order)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderRepo.findAll().spliterator(), false).filter(book -> {
            try {
                return (format.parse(book.getDateOfOrder()).getTime() >= fromDate.getTime() && format.parse(book.getDateOfOrder()).getTime() <= toDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).map(orderBooksEntityAssembler::toModel).collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class).getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * This method search for orders with less or equal number of books in order.
     *
     * @param numberOfBooks represents a number of books in order.
     * @return CollectionModel of EntityModels of OrderBooks.
     */
    @GetMapping("/orders/books")
    @Operation(summary = "Get all books with up to number of books list.")
    public ResponseEntity<CollectionModel<EntityModel<OrderBooks>>> getOrderWithLessThan(@RequestParam("numberOfBooks") int numberOfBooks) {
        List<EntityModel<OrderBooks>> books = StreamSupport.stream(booksOrderRepo.findAll().spliterator(), false)
                .filter(book -> book.getBooksList().size() <= numberOfBooks)
                .map(orderBooksEntityAssembler::toModel)
                .collect(Collectors.toList());
        if (books.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(OrderBooksController.class).getAllOrders()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for the city with the most of the deliveries above 100 ILS.
     *
     * @return list of Strings.
     */
    @GetMapping("/orders/city")
    @Operation(summary = "Get the city with the most deliveries above 100 ILS.")
    public List<String> getCityWithMaxDelivery() {
        Map<String, Integer> cityCount = new HashMap<>();
        List<String> citiesWithMaxDeliveries = new ArrayList<>();
        List<OrderBooks> list = StreamSupport.stream(booksOrderRepo.findAll().spliterator(), false)
                .filter(order -> order.getPrice() >= 100)
                .collect(Collectors.toList());

        for (int i = 0; i < list.size(); i++) {
            int deliveriesInCity = cityCount.containsKey(list.get(i).getCityOfDelivery()) ? cityCount.get(list.get(i).getCityOfDelivery()) + 1 : 1;
            cityCount.put(list.get(i).getCityOfDelivery(), deliveriesInCity);
        }

        int maxValue = (Collections.max(cityCount.values()));
        for (Map.Entry<String, Integer> map : cityCount.entrySet()) {
            if (map.getValue() == maxValue) citiesWithMaxDeliveries.add(map.getKey());
        }
        return citiesWithMaxDeliveries;
    }
}
