package restapi.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserInfoRepo extends CrudRepository<UserInfo,Long> {
    ResponseEntity<EntityModel<UserInfo>> getUserByFirstName(String firstName);
    UserInfo findByFirstName(String firstName);
    ResponseEntity<EntityModel<UserInfo>> findByEmail(String email);



}
