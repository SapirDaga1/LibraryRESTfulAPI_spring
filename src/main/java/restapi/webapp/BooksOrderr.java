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
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "BooksOrderr")
public class BooksOrderr {

   @Id @GeneratedValue private Long numberOfOrderr;

   //@JsonIgnore
   @JoinTable(name = "books_info",
           joinColumns = {@JoinColumn(name = "numberOfOrderr")},
           inverseJoinColumns = {@JoinColumn(name = "bookID")})
   @ManyToMany
   private List<BookInfo> booksList= new ArrayList<>();

   //@JsonIgnore
   @OneToOne private UserInfo user;

    public BooksOrderr(List<BookInfo> booksList,UserInfo user) {
        this.booksList = booksList;
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

