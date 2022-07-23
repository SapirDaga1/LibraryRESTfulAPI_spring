package restapi.webapp;

import io.swagger.v3.oas.annotations.Operation;
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
    private BookEntityAssembler bookEntityAssembler;
    private BookDTOAssembler bookDTOAssembler;

    public BookInfoController(BookInfoRepo bookInfoRepo, BookEntityAssembler bookEntityAssembler, BookDTOAssembler bookDTOAssembler) {
        this.bookInfoRepo = bookInfoRepo;
        this.bookEntityAssembler = bookEntityAssembler;
        this.bookDTOAssembler = bookDTOAssembler;
    }

    /**
     * This method search for all books from the database.
     * @return an iterable of BookInfo.
     */
    @GetMapping("/books")
    @Operation(summary = "Get all books.")
    public Iterable<BookInfo> getAllBooks() {
        return bookInfoRepo.findAll();
    }

    /**
     * This method search for a specific book from database by its id.
     * @param id of a specific book.
     * @return EntityModel of BookInfo.
     */
    @GetMapping("/book/{id}")
    @Operation(summary = "Get details of a specific book by id.")
    public EntityModel<BookInfo> getSpecificBook(@PathVariable Long id) {
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() ->
                new BookNotFoundException(id));
        return EntityModel.of(bookInfo,
                linkTo(methodOn(BookInfoController.class).getSpecificBook(id)).withSelfRel(),
                linkTo(methodOn(BookInfoController.class).getAllBooks()).withRel("back to all books"));
    }

    /**
     * This method gives us links of a specific book by its id.
     * @param id of a specific book.
     * @return EntityModel of BookDTO.
     */
    @GetMapping("/book/{id}/links")
    @Operation(summary = "Get links of specific bool by id.")
    public ResponseEntity<EntityModel<BookDTO>> getBookLinks(@PathVariable Long id) {
        return bookInfoRepo.findById(id).map(BookDTO::new).map(bookDTOAssembler::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * This method search for all the books that their amount of pages is under the parameter inserted.
     * @param pages represent an amount of pages to search up to them.
     * @return CollectionModel of EntityModels of BookInfo.
     */
    @GetMapping("/books/underAmountOfPages")
    @Operation(summary = "Get all books with less pages amount than insert from user.")
    public  ResponseEntity<CollectionModel<EntityModel<BookInfo>>>getBooksUnderPages(@RequestParam int pages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> book.getPageCount() < pages)
                .map(bookEntityAssembler::toModel).collect(Collectors.toList());
        if(books.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This method search for all the books with specific title.
     * @param title of a book
     * @return BookInfo.
     */

    @GetMapping("/book/title")
    @Operation(summary = "Get book by title")
    public ResponseEntity<BookInfo> getBookByTitle(@RequestParam("title") String title){
        BookInfo bookInfo= bookInfoRepo.findByTitle(title);
        if (bookInfo!=null) {
            return ResponseEntity.ok(bookInfo);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for a book with specific number of pages.
     * @param pages represent exact number of pages.
     * @return BookInfo.
     */
    @GetMapping("/book/amountOfPages")
    @Operation(summary = "Get book with specific amount of pages")
    public ResponseEntity<BookInfo> getBookByCountPages(@RequestParam("pages") int pages){
        BookInfo bookInfo= bookInfoRepo.findByPageCount(pages);
        if (bookInfo!=null) {
            return ResponseEntity.ok(bookInfo);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for all the books with specific publisher.
     * @param publisher of a books.
     * @return CollectionModel of EntityModels of BookInfo.
     */
    @GetMapping("/books/publishedBy/{publisher}")
    @Operation(summary = "Get all books that published by specific publisher.")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBookByPublisher(@PathVariable("publisher") String publisher) {
        List<BookInfo> books=bookInfoRepo.findByPublisher(publisher);
        if (books.size()!=0) {
            return ResponseEntity.ok(bookEntityAssembler.toCollectionModel(books));
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method delete a book with specific id.
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
     * This method search for all books in range of pages.
     * @param fromPages represents a lower threshold of pages.
     * @param toPages  represents an upper threshold of pages.
     * @return CollectionModel of EntityModels of BookInfo.
     */
    @GetMapping("/books/betweenPages")
    @Operation(summary = "Get all books in range of amount of pages.")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBooksBetweenPages(@RequestParam int fromPages, @RequestParam int toPages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                .map(bookEntityAssembler::toModel).collect(Collectors.toList());
        if(books.size()!=0){
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * This method search for all books in range of pages and sort it by number of pages in ascending order.
     * @param fromPages represents a lower threshold of pages.
     * @param toPages  represents an upper threshold of pages.
     * @return CollectionModel of EntityModels of BookInfo.
     */
    @GetMapping("/books/amountOfPages/sort")
    @Operation(summary = "Get all books in range of amount of pages sorted.")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBooksSort(@RequestParam int fromPages, @RequestParam int toPages) {
        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> (book.getPageCount() < toPages && book.getPageCount() > fromPages))
                .sorted().map(bookEntityAssembler::toModel).collect(Collectors.toList());
        if(books.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * This method search for all books that published between range of dates.
     * @param fromDate represents starting date to search from it.
     * @param toDate represent last date to search to it.
     * @return CollectionModel of EntityModels of BookInfo.
     * @throws Exception
     */
    @GetMapping("/books/betweenDates")
    @Operation(summary = "Get all books that was published between range of dates",description = "Please enter dates with format: yyyy-mm-dd ")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getBooksBetweenDates
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
                .map(bookEntityAssembler::toModel).collect(Collectors.toList());
        if (books.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for all books that published by specific publisher after specific date.
     * @param publisher represent a specific publisher.
     * @param afterDate represent date to start search from.
     * @return CollectionModel of EntityModels of BookInfo.
     */
    @GetMapping("/books/byPublisher/afterDate")
    @Operation(summary = "Get all books that published by specific publisher after specific date.",description = "Please enter dates with format: yyyy-mm-dd ")
    public ResponseEntity<CollectionModel<EntityModel<BookInfo>>> getByPublisherAndDate(@RequestParam("publisher") String publisher,
                                                                                        @RequestParam("afterDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<BookInfo>> books = bookInfoRepo.findAll()
                .stream().filter(book -> {
                    try {
                        return (Objects.equals(book.getPublisher(), publisher) && format.parse(book.getPublishedDate()).getTime() >= afterDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .sorted().map(bookEntityAssembler::toModel).collect(Collectors.toList());
        if (books.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(books, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

