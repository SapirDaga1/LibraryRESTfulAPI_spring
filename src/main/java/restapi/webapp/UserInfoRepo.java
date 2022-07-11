package restapi.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface UserInfoRepo extends CrudRepository<UserInfo,String> {
    ResponseEntity<EntityModel<UserDTO>> getUserByFirstName(String firstName);
}
