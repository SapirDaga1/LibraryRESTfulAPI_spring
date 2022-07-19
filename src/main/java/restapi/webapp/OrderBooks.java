package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

/**
 * This class represents an order of books.
 */
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "OrderBooks")
public class OrderBooks {


   @Id @GeneratedValue private Long id;
    //@JsonIgnore
    @OneToOne private UserInfo user;
    //@JsonIgnore
    @JoinTable(name = "books_info",
            joinColumns = {@JoinColumn(name = "numberOfOrderr")},
            inverseJoinColumns = {@JoinColumn(name = "bookID")})
    @ManyToMany
    private List<BookInfo> booksList= new ArrayList<>();
    private String dateOfOrder;
    private String cityOfDelivery;
    private int price;


    public OrderBooks(List<BookInfo> booksList, String dateOfOrder, String cityOfDelivery, UserInfo user,int price) {
        this.booksList = booksList;
        this.dateOfOrder=dateOfOrder;
        this.cityOfDelivery=cityOfDelivery;
        this.user=user;
        this.price=price;

    }

    public OrderBooks(OrderBooks orderBooks) {
    }
    @Override
    public String toString() {
        return "OrderBooks{" +
                "numberOfOrderr=" + getId() +
                ", dateOfOrder='" + getDateOfOrder() + '\'' +
                ", cityOfDelivery='" + getCityOfDelivery() + '\'' +
                ", booksList=" + getBooksList() +
                ", user=" + getUser() +
                '}';
    }

}

