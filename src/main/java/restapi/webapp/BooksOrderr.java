package restapi.webapp;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Set;

/**
 * This class represents an order of books.
 */
@Entity
@Data
@NoArgsConstructor
public class BooksOrderr {
    @Id @GeneratedValue
    private int numberOfOrderr;
    //Value annotation for immutable types.
    private List<BookDTO> booksList;

    public BooksOrderr(int numberOfOrderr) {
        this.numberOfOrderr = numberOfOrderr;
       // this.booksList = booksList;
    }
}

