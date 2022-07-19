package restapi.webapp;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;
import net.bytebuddy.asm.Advice;
import org.apache.catalina.User;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    @Operation(summary = "Get all users.")
    public Iterable<UserInfo> getAllUsers() {
        return userInfoRepo.findAll();
    }

    /**
     * @param id- represents the id of user.
     * @return information/details about this user.
     */
    @GetMapping("/user/{id}")
    @Operation(summary = "Get a specific user by id.")
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
    @GetMapping("/user/{id}/links")
    @Operation(summary = "Get links of a specific user by id.")
    public ResponseEntity<EntityModel<UserDTO>> userDetails(@PathVariable Long id) {
        return userInfoRepo.findById(id).map(UserDTO::new).map(userDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/byFirstName")
    @Operation(summary = "Get users by first name.")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByFirstName(@RequestParam("firstName") String firstName) {

            List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findByFirstName(firstName).spliterator(), false)
                    .map(userEntityFactory::toModel).collect(Collectors.toList());
            if(users.size()!=0) {
                return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(BookInfoController.class)
                        .getAllBooks()).withSelfRel()));
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }
    /**
     *
     * @param lastName of specific user
     * @return details of this user if exist or error message if it doesn't.
     */
    @GetMapping("/user/byLastName")
    @Operation(summary = "Get users by last name.")
    //return only one with this lastName, if there is more than one we get status code 500
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByLastName(@RequestParam("lastName") Optional<String> lastName) {
        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findByLastName(lastName).spliterator(), false)
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if(users.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param email of specific user
     * @return details of this user if exist or error message if it doesn't.
     */
    @GetMapping("/user/byEmail/{email}")
    @Operation(summary = "Get user by email.")
    public ResponseEntity<UserInfo> getUserByEmail(@PathVariable("email") String email) {
        return Optional.ofNullable(userInfoRepo.findByEmail(email))
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new UserNotFoundException(email));
    }

    /**
     *
     * @param fromDate to search.
     * @param toDate to search.
     * @return Collection<EntityModel<UserInfo>>> of users that was born between (fromDate,toDate).
     * @throws Exception
     */
    @GetMapping("/users/birthdayDates/betweenDates")
    @Operation(summary = "Get all users that was born between range of dates.",description = "Please enter dates with format: yyyy-mm-dd ")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserBirthBetweenDates
            (@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
             @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception {

        //this is the format of the date we want to use(filter the date of birthday)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findAll().spliterator(), false)
                .filter(user -> { try {
                        return (format.parse(user.getDateOfBirth()).getTime() >= fromDate.getTime() &&
                                format.parse(user.getDateOfBirth()).getTime() <= toDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if(users.size()!=0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(UserController.class)
                    .getAllUsers()).withSelfRel()));
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     *
     * @param userInfo -details of a new user.
     * @return status code 201 - create user successfully.
     */
    @PostMapping("/users/add")
    @Operation(summary = "Add new user.")
    public ResponseEntity<EntityModel<UserInfo>> addUser(@RequestBody UserInfo userInfo){

            UserInfo savedUser = userInfoRepo.save(userInfo);
            return ResponseEntity.created(linkTo(methodOn(UserController.class)
                            .getSpecificUser(savedUser.getId())).toUri())
                    .body(userEntityFactory.toModel(savedUser));

    }

    /**
     *
     * @param firstName of a specific user.
     * @param lastName of a specific user.
     * @return CollectionModel<EntityModel<UserInfo>> with the full name - {firstName+lastName}.
     */
    @GetMapping("/users/byFullName")
    @Operation(summary = "Get all users by firstName + lastName.")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getuserByFullName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findAll().spliterator(), false)
                .filter(user -> (Objects.equals(user.getFirstName(), firstName) && Objects.equals(user.getLastName(), lastName)))
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if (users.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(UserController.class)
                    .getAllUsers()).withSelfRel()));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //TODO: Methods with complex segmentations
    //average and median of age

    //@GetMapping("/users/averageAge")
    //@GetMapping("/users/medianAge")


}
