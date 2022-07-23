package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

/**
 * This class represent metadata of a book.
 */
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfo implements Comparable<BookInfo> {

    private @Id
    @GeneratedValue Long bookID;
    private String title;
    private String id;
    @ElementCollection
    private List<String> authors = new ArrayList<>();
    private String publisher;
    private String publishedDate;
    private int pageCount;
    private String language;
    private String contentVersion;

    @JsonIgnore
    @ManyToMany(mappedBy = "booksList")
    private List<OrderBooks> booksOrders = new ArrayList<>();

    public BookInfo(String title, String id, List<String> authors, String publisher, String publishedDate, int pageCount, String language, String contentVersion) {
        this.title = title;
        this.id = id;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.language = language;
        this.contentVersion = contentVersion;
    }

    // Use this for get the relevant properties from the api, due to the JSON structure.
    @JsonProperty("volumeInfo")
    private void unpackedVolumeInfo(Map<String, Object> volumeInfo) {
        this.title = (String) volumeInfo.get("title");
        this.authors = (List<String>) volumeInfo.get("authors");
        this.publisher = (String) volumeInfo.get("publisher");
        this.publishedDate = getValidDate((String) volumeInfo.get("publishedDate"));
        this.pageCount = (int) volumeInfo.get("pageCount");
        this.language = (String) volumeInfo.get("language");
        this.contentVersion = (String) volumeInfo.get("contentVersion");
    }

    @JsonProperty("items")
    private void unpackedItems(List<Map<String, Object>> items) {
        this.id = (String) items.get(0).get("id");
        unpackedVolumeInfo((Map<String, Object>) items.get(0).get("volumeInfo"));
    }

    @Override
    public int compareTo(BookInfo otherBook) {
        return Double.compare(this.getPageCount(), otherBook.getPageCount());
    }

    private String getValidDate(String date) {
        if (date.length() == 4) {
            return date + "-01-01";
        }
        return date;
    }

}



