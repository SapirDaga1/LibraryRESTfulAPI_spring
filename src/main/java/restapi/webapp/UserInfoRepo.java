package restapi.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepo extends CrudRepository<UserInfo,Long> {
    UserInfo findByEmail(String email);
    List<UserInfo> findByFirstName(String firstName);
    UserInfo findByLastName(Optional<String> firstName);




}
