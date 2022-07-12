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
@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksOrderr {
    //maybe we should put @id as user's order.(String)
    @Id private Long numberOfOrderr;

   @JsonIgnore
   @ManyToMany(mappedBy ="booksOrderrs")
   private Set<BookInfo> booksList=new HashSet<>();

    public BooksOrderr(Long numberOfOrderr, Set<BookInfo> booksList) {
        this.numberOfOrderr = numberOfOrderr;
        this.booksList = booksList;
    }
}

