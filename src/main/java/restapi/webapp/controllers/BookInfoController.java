package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.assemblers.BookDTOFactory;
import restapi.webapp.assemblers.BookEntityFactory;
import restapi.webapp.assemblers.BookInfoRepo;
import restapi.webapp.dto.BookDTO;
import restapi.webapp.exceptions.BookNotFoundException;
import restapi.webapp.pojo.BookInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class BookInfoController {
    private BookInfoRepo bookInfoRepo;
    private BookEntityFactory bookEntityFactory;
    private BookDTOFactory bookDTOFactory;

    public BookInfoController(BookInfoRepo bookInfoRepo, BookEntityFactory bookEntityFactory, BookDTOFactory bookDTOFactory) {
        this.bookInfoRepo = bookInfoRepo;
        this.bookEntityFactory = bookEntityFactory;
        this.bookDTOFactory = bookDTOFactory;
    }

    /**
     * @return an iterable of all books.
     */
    @GetMapping("/books")
    @Operation(summary = "Get all books.")
    public Iterable<BookInfo> getAllBooks() {
        return bookInfoRepo.findAll();
    }

    /**
     * @param id of a specific book
     * @return link to specific book by it id and link to all the books
     */
    @GetMapping("/book/{id}")
    @Operation(summary = "Get specific book by id.")
    public EntityModel<BookInfo> getSpecificBook(@PathVariable Long id) {
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() ->
                new BookNotFoundException(id));
        return EntityModel.of(bookInfo,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(id)).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back to all books"));
    }

    /**
     * @param id of a specific book.
     * @return information/details about this book.
     */
    @GetMapping("/book/{id}/info")
    @Operation(summary = "Get links of specific bool by id.")
    public ResponseEntity<EntityModel<BookDTO>> bookDetails(@PathVariable Long id) {
        return bookInfoRepo.findById(id).map(BookDTO::new).map(bookDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/books/underAmountOfPages")
    @Operation(summary = "Get all books with less pages amount than insert from user.")
    public  ResponseEntity<CollectionModel<EntityModel<BookInfo>>>getBooksUnderPages(@RequestParam int pages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> book.getPageCount() < pages)
                .map(bookEntityFactory::toModel).collect(Collectors.toList());
        return  ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel()));
    }

    /**
     *
     * @param title of a book
     * @return the details of book with this {title}.
     */

    @GetMapping("/book/title")
    @Operation(summary = "Get book by title")
    public ResponseEntity<BookInfo> getBookByTitle(@RequestParam("title") String title){
        BookInfo bookInfo= bookInfoRepo.findByTitle(title);
        return ResponseEntity.ok(bookInfo);
    }

    /**
     *
     * @param pages represent exact number of pages.
     * @return book with number of {pages} pages.
     */
    @GetMapping("/book/amountOfPages")
    @Operation(summary = "Get book with specific amount of pages")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BookInfo> getBookByCountPages(@RequestParam("pages") int pages){
        BookInfo bookInfo= bookInfoRepo.findByPageCount(pages);
        return ResponseEntity.ok(bookInfo);
    }
    @GetMapping("/books/publishedBy/{publisher}")
    @Operation(summary = "Get all books that published by specific publisher.")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBookByPublisher(@PathVariable("publisher") String publisher) {
        return ResponseEntity.ok(
                bookEntityFactory.toCollectionModel(bookInfoRepo.findByPublisher(publisher)));
    }

    /**
     * @param id of specific book.
     * @return response of deleting the book.
     * @throws BookNotFoundException if book does not exist.
     */
    @DeleteMapping("/book/{id}")
    @Operation(summary = "Delete a book by id.")
    public Map<String, Boolean> deleteBook(@PathVariable(value = "id") Long id)
            throws BookNotFoundException {
        BookInfo book = bookInfoRepo.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookInfoRepo.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    /**
     * @param fromPages min amount of pages
     * @param toPages   max amount of pages
     * @return all the books with page amount between range: (fromPages, toPages)
     */
    @GetMapping("/books/betweenPages")
    @Operation(summary = "Get all books in range of amount of pages.")
    public CollectionModel<EntityModel<BookInfo>> getBooksBetweenPages(@RequestParam int fromPages, @RequestParam int toPages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                .map(bookEntityFactory::toModel).collect(Collectors.toList());
        return CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel());
    }

    /**
     *
     * @param fromPages min amount of pages
     * @param toPages  max amount of pages
     * @return sorted list of books that are between the same pages by the page count.
     */
    @GetMapping("/books/amountOfPages/sort")
    @Operation(summary = "Get all books in range of amount of pages sorted.")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBooksSort(@RequestParam int fromPages, @RequestParam int toPages) {
        List<EntityModel<BookInfo>> products = bookInfoRepo.findAll()
                .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                .sorted().map(bookEntityFactory::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(products,linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel()));
    }

    /**
     *
     * @param fromDate represents starting date looking for
     * @param toDate represent last date looking for
     * @return list of entity model of bookInfo that published between range of dates/
     * @throws Exception
     */
    @GetMapping("/books/betweenDates")
    @Operation(summary = "Get all books that was published between range of dates",description = "Please enter dates with format: yyyy-mm-dd ")
    public CollectionModel<EntityModel<BookInfo>> getBooksBetweenDates
    (@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
     @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception{

        //this is the format of the date we want to use(filter the date of book from database to this format)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> {
                    try {
                        return (format.parse(book.getPublishedDate()).getTime()>= fromDate.getTime() &&
                                format.parse(book.getPublishedDate()).getTime()<= toDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }return false;
                })
                .map(bookEntityFactory::toModel).collect(Collectors.toList());
        return CollectionModel.of(books,linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel());
    }

    /**
     *
     * @param publisher represent a specific publisher.
     * @param afterDate represent date to start search from.
     * @return all the books that published by {publisher} after date {afterDate}.
     */
    @GetMapping("/books/byPublisher/afterDate")
    @Operation(summary = "Get all books that published by specific publisher after specific date.",description = "Please enter dates with format: yyyy-mm-dd ")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getByPublisherAndDate(@RequestParam("publisher") String publisher,
                                                                                        @RequestParam("afterDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<BookInfo>> products = bookInfoRepo.findAll()
                .stream().filter(book -> {
                    try {
                        return (Objects.equals(book.getPublisher(), publisher) && format.parse(book.getPublishedDate()).getTime() >= afterDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .sorted().map(bookEntityFactory::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(products,linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel()));
    }
    }

