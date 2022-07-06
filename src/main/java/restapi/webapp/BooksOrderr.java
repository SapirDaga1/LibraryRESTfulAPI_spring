package restapi.webapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * This class represents an order of books.
 */
@Entity
@Data
@NoArgsConstructor
public class BooksOrderr {
    @Id @GeneratedValue
    private int numberOfOrderr;
    //private List<BookDTO> booksList;

    public BooksOrderr(int numberOfOrderr) {
        this.numberOfOrderr = numberOfOrderr;
       // this.booksList = booksList;
    }
}

