package restapi.webapp;

import org.apache.catalina.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserInfoRepo userInfoRepo;
    private UserDTOFactory userDTOFactory;
    private UserEntityFactory userEntityFactory;

    public UserController(UserInfoRepo userInfoRepo, UserDTOFactory userDTOFactory,UserEntityFactory userEntityFactory) {
        this.userInfoRepo = userInfoRepo;
        this.userDTOFactory = userDTOFactory;
        this.userEntityFactory = userEntityFactory;

    }

    // 3 GET methods and 1 of other CRUD methods.

    /**
     * @return all users in the database
     */
    @GetMapping("/users")
    public Iterable<UserInfo> getAllUsers() {
        return userInfoRepo.findAll();
    }

    /**
     * @param email represents the id of user.
     * @return user links by his id.
     */
    @GetMapping("/user/{id}")
    public EntityModel<UserInfo> getSpecificUser(@PathVariable String email) {
        UserInfo userInfo = userInfoRepo.findById(email).orElseThrow(() ->
                new UserNotFoundException(email));
        return EntityModel.of(userInfo,
                linkTo(methodOn(UserController.class).getSpecificUser(email)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("back to all users"));
    }

    @GetMapping("/user/{id}/details")
    public ResponseEntity<EntityModel<UserDTO>> userDetails(@PathVariable String id) {
        return userInfoRepo.findById(id).map(UserDTO::new).map(userDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/user")
//    @ResponseStatus(HttpStatus.OK)
//    public CollectionModel<EntityModel<UserInfo>> getUserByFirstName(@RequestParam String firstName) {
//        List<EntityModel<UserInfo>> users = userInfoRepo.findAll()
//                .stream().filter(user -> user == firstName)
//                .map(userEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(users, linkTo(methodOn(UserController.class)
//                .getAllUsers()).withSelfRel());
//
//
//    }
//@GetMapping("/user/name")
//@ResponseStatus(HttpStatus.OK)
//public ResponseEntity<EntityModel<UserDTO>> getUserByFirstN(@RequestParam String firstName){
//    ResponseEntity<EntityModel<UserDTO>> user = userInfoRepo.getFirstName(firstName);
//    return user;
//}

//    @GetMapping("/users/fullname")
//    @ResponseStatus(HttpStatus.OK)
//    //@ResponseBody
//    public CollectionModel<EntityModel<UserInfo>> getuserByFullName(@RequestParam String firstName, @RequestParam String lastName){
//        List<EntityModel<UserInfo>> users = userInfoRepo.findAll()
//                .stream().filter(user -> (user.getFirstName()==firstName && user.getLastName()==lastName))
//                .map(userEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(users,linkTo(methodOn(UserController.class)
//                .getuserByFullName(firstName,lastName)).withSelfRel());
//    }
}
