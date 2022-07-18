package restapi.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import restapi.webapp.pojo.UserInfo;

@Value
@JsonIgnoreProperties({"email","firstName","lastName","dateOfBirth"})
public class UserDTO {

    @JsonIgnore
    private final UserInfo user;

    public String getEmail(){return this.user.getEmail();}
    public String getFirstName(){return this.user.getFirstName();}
    public String getLastName(){return this.user.getLastName();}
    public String getDateOfBirth(){ return this.user.getDateOfBirth();}

}
