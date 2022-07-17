package restapi.webapp;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Iterable<BookInfo> getAllBooks() {
        return bookInfoRepo.findAll();
    }
    //    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getAllBooks() {
//        return ResponseEntity.ok(bookEntityFactory.toCollectionModel(bookInfoRepo.findAll()));
//    }

    /**
     * @param id of a specific book
     * @return link to specific book by it id and link to all the books
     */
    @GetMapping("/book/{id}")
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
    public ResponseEntity<EntityModel<BookDTO>> bookDetails(@PathVariable Long id) {
        return bookInfoRepo.findById(id).map(BookDTO::new).map(bookDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //TODO:fix the issue with that it returns always status 200
    @GetMapping("/books/underpages")
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
    @ResponseStatus(HttpStatus.OK)
    public BookInfo getBookByTitle(@RequestParam("title") String title){
        BookInfo bookInfo= bookInfoRepo.findByTitle(title);
        return bookInfo;
    }

    /**
     *
     * @param pages represent exact number of pages.
     * @return book with number of {pages} pages.
     */
    @GetMapping("/book/pages")
    @ResponseStatus(HttpStatus.OK)
    public BookInfo getBookByCountPages(@RequestParam("pages") int pages){
        BookInfo bookInfo= bookInfoRepo.findByPageCount(pages);
        return bookInfo;
    }
    @GetMapping("/books/publishedBy/{publisher}")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBookByPublisher(@PathVariable("publisher") String publisher) {
        return ResponseEntity.ok(
                bookEntityFactory.toCollectionModel(bookInfoRepo.findByPublisher(publisher)));
    }



//    public EntityModel<BookInfo> getBookByTitle(@RequestParam("title") String title) {
//        EntityModel<BookInfo> book = bookInfoRepo.findByTitle(title);
//        return book;
//
//    }

    //TODO: add a message to request body
    /**
     * @param id of specific book.
     * @return response of deleting the book.
     * @throws BookNotFoundException if book does not exist.
     */
    @DeleteMapping("/book/{id}")
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
    @GetMapping("/books/betweenpages")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<BookInfo>> getBooksBetweenPages(@RequestParam int fromPages, @RequestParam int toPages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                .map(bookEntityFactory::toModel).collect(Collectors.toList());
        return CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                .getAllBooks()).withSelfRel());
    }
    /**
     *
     * @param fromDate represents starting date looking for
     * @param toDate represent last date looking for
     * @return list of entity model of bookInfo that published between range of dates/
     * @throws Exception
     */
    @GetMapping("/books/betweenDates")
    @ResponseStatus(HttpStatus.OK)
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

    //TODO: Methods with complex segmentations
    //sort list of books that are between the same pages by the page count.
    @GetMapping("/books/sort")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<BookInfo>> getBooksSort(@RequestParam int fromPages, @RequestParam int toPages) {
            List<EntityModel<BookInfo>> products = bookInfoRepo.findAll()
                    .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                    .sorted().map(bookEntityFactory::toModel).collect(Collectors.toList());
            return CollectionModel.of(products,linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel());
        }
    }

