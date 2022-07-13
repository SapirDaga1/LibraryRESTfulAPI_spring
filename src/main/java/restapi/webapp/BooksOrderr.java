package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents an order of books.
 */
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "orders")
public class BooksOrderr {

   @Id @GeneratedValue private Long numberOfOrderr;

   @JsonIgnore
   @ManyToMany(mappedBy ="booksOrderrs")
   private List<BookInfo> booksList= new ArrayList<>();

    public BooksOrderr(List<BookInfo> booksList) {
        this.booksList = booksList;
    }
}

