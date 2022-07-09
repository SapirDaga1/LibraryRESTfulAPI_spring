package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represent metadata of a book.
 * It's the strong entity - without books we cannot make orders or borrows.
 */

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfo {

    private @Id @GeneratedValue Long bookID; //book id as we create
    private String id; // book id from the api
    private String title;
    private String publisher;
    private String description;
   //private int count;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "books_info",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "orderr_id")})
    Set<BooksOrderr> booksOrderrs = new HashSet<>();

    public BookInfo(Long bookID) {
        this.bookID = bookID;
    }

    public BookInfo(Long bookID,String id, String title, String description,String publisher) {
            this.bookID=bookID;
            this.id =id;
            this.title = title;
            this.description = description;
            this.publisher = publisher;

    }

}



