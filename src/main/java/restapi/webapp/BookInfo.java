package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    public BookInfo(Long bookID,int shelve, String title, String description, int count) {

        this.bookID=bookID;
        this.shelve = shelve;
        this.title = title;
        this.description = description;
        this.count = count;
    }
}



