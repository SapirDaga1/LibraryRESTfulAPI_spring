package restapi.webapp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * This class represents an order of books.
 */
public class BooksOrder {
    @Id @GeneratedValue
    private int numberOfOrder;
    private List<BookDTO> booksList;

    public BooksOrder(int numberOfOrder, List<BookDTO> booksList) {
        this.numberOfOrder = numberOfOrder;
        this.booksList = booksList;
    }
}

