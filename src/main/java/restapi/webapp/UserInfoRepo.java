package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepo extends CrudRepository<UserInfo,String> {
}
