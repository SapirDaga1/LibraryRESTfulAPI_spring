package restapi.webapp.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    public UserInfo(String email, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    //TODO: connection user to order
}
