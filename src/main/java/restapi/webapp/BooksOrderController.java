package restapi.webapp;

import org.springframework.web.bind.annotation.GetMapping;

public class BooksOrderController {
    private BooksOrderRepo booksOrderRepo;
    private BooksOrderEntityFactory booksOrderEntityFactory;

    public BooksOrderController(BooksOrderRepo booksOrderRepo, BooksOrderEntityFactory booksOrderEntityFactory) {
        this.booksOrderRepo = booksOrderRepo;
        this.booksOrderEntityFactory = booksOrderEntityFactory;
    }
    //TODO: 3 GET methods and 1 of other CRUD methods.
//    @GetMapping("/orders")
//
//    @GetMapping("/order/{numberOfOrder}")
//
//    @GetMapping("/order/{numberOfOrder}/info")
}
