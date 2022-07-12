package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties({"email","firstName","lastName"})
public class UserDTO {

    @JsonIgnore
    private final UserInfo user;

    public String getFirstName(){return this.user.getFirstName();}
    public String getLastName(){return this.user.getLastName();}
    public String getEmail(){return this.user.getEmail();}
}
