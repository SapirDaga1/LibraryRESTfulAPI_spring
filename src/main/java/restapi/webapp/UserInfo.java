package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class UserInfo {
    private @Id @GeneratedValue
    Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;

    public UserInfo(String email, String firstName, String lastName, String phoneNumber,String city) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.city=city;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private BooksOrderr orderr;
}
