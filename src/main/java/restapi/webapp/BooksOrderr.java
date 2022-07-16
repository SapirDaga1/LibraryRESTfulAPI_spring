package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.awt.print.Book;
import java.util.*;

/**
 * This class represents an order of books.
 */
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "BooksOrderr")
public class BooksOrderr {

   @Id @GeneratedValue private Long numberOfOrderr;
   private String dateOfOrderr;
   private String cityOfDelivery;

   //@JsonIgnore
   @JoinTable(name = "books_info",
           joinColumns = {@JoinColumn(name = "numberOfOrderr")},
           inverseJoinColumns = {@JoinColumn(name = "bookID")})
   @ManyToMany
   private List<BookInfo> booksList= new ArrayList<>();

   //@JsonIgnore
   @OneToOne private UserInfo user;

    public BooksOrderr(List<BookInfo> booksList,String dateOfOrderr,String cityOfDelivery,UserInfo user) {
        this.booksList = booksList;
        this.dateOfOrderr=dateOfOrderr;
        this.cityOfDelivery=cityOfDelivery;
        this.user=user;
    }


//    @Override
//    public String toString() {
//        return "BooksOrderr{" +
//                "numberOfOrderr=" + getNumberOfOrderr() +
//                ", booksList=" + getBooksList() +
//                ", user=" + getUser() +
//                '}';
//    }
}

