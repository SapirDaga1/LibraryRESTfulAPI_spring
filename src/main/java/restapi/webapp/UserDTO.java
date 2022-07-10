package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties({"firstName","lastName","email"})
public class UserDTO {

    @JsonIgnore
    private final UserInfo user;

    private String getFirstName(){return this.user.getFirstName();}
    private String getLastName(){return this.user.getLastName();}
    private String getEmail(){return this.user.getEmail();}
}
