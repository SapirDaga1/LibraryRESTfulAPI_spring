package restapi.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserInfoRepo userInfoRepo;
    private UserDTO userDTO;

    public UserController(UserInfoRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;

    }

    // 3 GET methods and 1 of other CRUD methods.
    @GetMapping("/users")
    public Iterable<UserInfo> getAllUsers(){return userInfoRepo.findAll();}
}
