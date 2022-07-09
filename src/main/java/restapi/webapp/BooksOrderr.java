package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class represents an order of books.
 */
@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksOrderr {
    @Id @GeneratedValue
    private int numberOfOrderr;

   @JsonIgnore
   @ManyToMany(mappedBy ="booksOrderrs")
    private List<BookDTO> booksList=new ArrayList<>();

    public BooksOrderr(int numberOfOrderr) {
        this.numberOfOrderr = numberOfOrderr;
       // this.booksList = booksList;
    }
}

