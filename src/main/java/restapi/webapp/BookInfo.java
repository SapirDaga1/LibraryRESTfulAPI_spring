package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represent metadata of a book.
 * It's the strong entity - without books we cannot make orders or borrows.
 */

@Entity
@Data
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfo {

    private @Id @GeneratedValue
    Long bookID; //book id as we create
    private int shelve; // book id from the api
    private String title;
    private String description;
    private int count;

    //TODO: many-to-many connection in our database.
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "books_info",
        joinColumns = {@JoinColumn(name = "book_id")},
        inverseJoinColumns = {@JoinColumn(name = "orderr_id")})
    Set<BooksOrderr> booksOrderrs = new HashSet<>();

    public BookInfo(Long bookID){ this.bookID=bookID;}

    public BookInfo(Long bookID,int shelve, String title, String description, int count) {

        this.bookID=bookID;
        this.shelve = shelve;
        this.title = title;
        this.description = description;
        this.count = count;
    }
}



