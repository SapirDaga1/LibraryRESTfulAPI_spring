package restapi.webapp;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserInfoRepo userInfoRepo;
    private UserDTOFactory userDTOFactory;
    private UserDTO userDTO;

    public UserController(UserInfoRepo userInfoRepo,UserDTOFactory userDTOFactory) {
        this.userInfoRepo = userInfoRepo;
        this.userDTOFactory=userDTOFactory;

    }

    // 3 GET methods and 1 of other CRUD methods.

    /**
     *
     * @return all users in the database
     */
    @GetMapping("/users")
    public Iterable<UserInfo> getAllUsers(){return userInfoRepo.findAll();}

    /**
     *
     * @param email represents the id of user.
     * @return user links by his id.
     */
    @GetMapping("/user/{id}")
    public EntityModel<UserInfo> getSpecificUser(@PathVariable String email){
        UserInfo userInfo = userInfoRepo.findById(email).orElseThrow(()->
                new UserNotFoundException(email));
        return EntityModel.of(userInfo,
                linkTo(methodOn(UserController.class).getSpecificUser(email)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("back to all users"));
    }
    @GetMapping("/user/{id}/details")
    public ResponseEntity<EntityModel<UserDTO>> userDetails(@PathVariable String id){
        return userInfoRepo.findById(id).map(UserDTO::new).map(userDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


}
