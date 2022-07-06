package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties({"bookID","title","description"})
public class BookDTO {

    @JsonIgnore
    private final BookInfo book;

    public Long getID(){ return this.book.getBookID();}
    public String getTitle() {return this.book.getTitle();}
    public String getDescription(){return this.book.getDescription();}

}
