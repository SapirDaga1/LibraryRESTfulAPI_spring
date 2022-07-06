package restapi.webapp;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksOrderrController {
    private BooksOrderrRepo booksOrrderRepo;
    private BooksOrderrEntityFactory booksOrderrEntityFactory;

    public BooksOrderrController(BooksOrderrRepo booksOrrderRepo, BooksOrderrEntityFactory booksOrderrEntityFactory) {
        this.booksOrrderRepo = booksOrrderRepo;
        this.booksOrderrEntityFactory = booksOrderrEntityFactory;
    }

    //TODO: 3 GET methods and 1 of other CRUD methods.
//    @GetMapping("/orders")
//
//    @GetMapping("/order/{numberOfOrder}")
//
//    @GetMapping("/order/{numberOfOrder}/info")
}
