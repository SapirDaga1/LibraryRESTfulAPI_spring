package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties({"bookID", "title", "publisher", "publishDate", "pageCount", "language"})
public class BookDTO {

    @JsonIgnore
    private final BookInfo book;

    public Long getID() {
        return this.book.getBookID();
    }

    public String getTitle() {
        return this.book.getTitle();
    }

    public String getPublisher() {
        return this.book.getPublisher();
    }

    public String getPublishedDate() {
        return this.book.getPublishedDate();
    }

    public int getPageCount() {
        return this.book.getPageCount();
    }

    public String getLanguage() {
        return this.book.getLanguage();
    }


}
