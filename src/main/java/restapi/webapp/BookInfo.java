package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
    private String publishedDate;
    private int pageCount;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "books_info",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "orderr_id")})
    Set<BooksOrderr> booksOrderrs = new HashSet<>();

    public BookInfo(String id, String title, String publisher, String publishedDate, int pageCount) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
    }

    // Use this for get the relevant properties from the api, due to the JSON structure.
    @JsonProperty("volumeInfo")
    private void unpackedVolumeInfo(Map<String, Object> volumeInfo){
        this.title=(String) volumeInfo.get("title");
        this.publisher=(String) volumeInfo.get("publisher");
        this.publishedDate=(String) volumeInfo.get("publishedDate");
        this.pageCount=(int) volumeInfo.get("pageCount");

    }

}



