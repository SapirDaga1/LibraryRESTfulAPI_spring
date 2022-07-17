package restapi.webapp;

import org.apache.catalina.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
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

    /**
     * @return all users in the database
     */
    @GetMapping("/users")
    public Iterable<UserInfo> getAllUsers() {
        return userInfoRepo.findAll();
    }

    /**
     * @param id- represents the id of user.
     * @return information/details about this user.
     */
    @GetMapping("/user/{id}")
    public EntityModel<UserInfo> getSpecificUser(@PathVariable Long id) {
        UserInfo userInfo = userInfoRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException(id));
        return EntityModel.of(userInfo,
                linkTo(methodOn(UserController.class).getSpecificUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("back to all users"));
    }

    /**
     *
     * @param id - represents the id of user.
     * @return user links by his id.
     */
    @GetMapping("/user/{id}/details")
    public ResponseEntity<EntityModel<UserDTO>> userDetails(@PathVariable Long id) {
        return userInfoRepo.findById(id).map(UserDTO::new).map(userDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //TODO:fix the issue with that it returns always status 200
    @GetMapping("/user/byFirstName")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByFirstName(@RequestParam("firstName") String firstName) {
        return ResponseEntity.ok(
                userEntityFactory.toCollectionModel(userInfoRepo.findByFirstName(firstName)));
    }
    //TODO:fix the issue with that it returns always status 200
    @GetMapping("/user/byLastName")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByLastName(@RequestParam("lastName") String lastName) {
        return ResponseEntity.ok(
                userEntityFactory.toCollectionModel(userInfoRepo.findByLastName(lastName)));
    }

    //TODO: fix error 500 is the email doesn't exist.
    @GetMapping("/user/byEmail")
    public ResponseEntity<EntityModel<UserInfo>> getUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(EntityModel.of(userInfoRepo.findByEmail(email)));
    }

    //TODO: fix this methods.
//    @GetMapping("/user")
//    @ResponseStatus(HttpStatus.OK)
//    public CollectionModel<EntityModel<UserInfo>> getUserByFirstName(@RequestParam String firstName) {
//        List<EntityModel<UserInfo>> users = userInfoRepo.findAll()
//                .stream().filter(user -> user.getFirstName() ==firstName)
//                .map(userEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(users, linkTo(methodOn(UserController.class)
//                .getAllUsers()).withSelfRel());
//
//
//    }

//}



    @PostMapping("/users/add")
    public ResponseEntity<EntityModel<UserInfo>> addUser(@RequestBody UserInfo userInfo){

            UserInfo savedUser = userInfoRepo.save(userInfo);
            return ResponseEntity.created(linkTo(methodOn(UserController.class)
                            .getSpecificUser(savedUser.getId())).toUri())
                    .body(userEntityFactory.toModel(savedUser));

    }
    //TODO: add 2 methods with 2 request param
// @GetMapping("/users/fullname")
//    @ResponseStatus(HttpStatus.OK)
//    //@ResponseBody
//    public CollectionModel<EntityModel<UserInfo>> getuserByFullName(@RequestParam String firstName, @RequestParam String lastName){
//        List<EntityModel<UserInfo>> users = userInfoRepo.findAll()
//                .stream().filter(user -> (user.getFirstName()==firstName && user.getLastName()==lastName))
//                .map(userEntityFactory::toModel).collect(Collectors.toList());
//        return CollectionModel.of(users,linkTo(methodOn(UserController.class)
//                .getuserByFullName(firstName,lastName)).withSelfRel());
//    }
    //TODO: Methods with complex segmentations
}
