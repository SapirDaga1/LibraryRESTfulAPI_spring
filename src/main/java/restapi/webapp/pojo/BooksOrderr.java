package restapi.webapp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
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

