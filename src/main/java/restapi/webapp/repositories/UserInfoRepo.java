package restapi.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.entities.UserInfo;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepo extends CrudRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);

    List<UserInfo> findByFirstName(String firstName);

    List<UserInfo> findByLastName(Optional<String> firstName);
}
