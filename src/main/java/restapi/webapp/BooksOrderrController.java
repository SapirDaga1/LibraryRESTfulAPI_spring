package restapi.webapp;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping("/orders")
//
//    @GetMapping("/order/{numberOfOrder}")
//
//    @GetMapping("/order/{numberOfOrder}/info")

//    @PostMapping("/newOrder")
//    public BooksOrderr newOrder(@Valid @RequestBody BookDTO book){
//        return BooksOrderrRepo.save(book);}

//    @PostMapping("/books")
//    public BooksOrderr addNewBook (@Valid @RequestBody BookDTO newBook){ // Relied on Jackson component for serialization
//        return booksOrderrRepo.save(newBook); // Relied on Jackson component for deserialization
//    }
}

