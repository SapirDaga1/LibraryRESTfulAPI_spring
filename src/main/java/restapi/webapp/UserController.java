package restapi.webapp;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    int CURRYEAR = 2022;

    private UserInfoRepo userInfoRepo;
    private UserDTOAssembler userDTOFactory;
    private UserEntityAssembler userEntityFactory;

    public UserController(UserInfoRepo userInfoRepo, UserDTOAssembler userDTOFactory, UserEntityAssembler userEntityFactory) {
        this.userInfoRepo = userInfoRepo;
        this.userDTOFactory = userDTOFactory;
        this.userEntityFactory = userEntityFactory;

    }

    /**
     * This method search for all users from the database.
     * @return an iterable of UserInfo.
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users.")
    public Iterable<UserInfo> getAllUsers() {
        return userInfoRepo.findAll();
    }

    /**
     * This method search for a specific user from database by its id.
     * @param id of a specific user.
     * @return EntityModel of UserInfo.
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
     * This method gives us links of a specific user by its id.
     * @param id of a specific user.
     * @return EntityModel of UserDTO.
     */
    @GetMapping("/user/{id}/links")
    @Operation(summary = "Get links of a specific user by id.")
    public ResponseEntity<EntityModel<UserDTO>> userDetails(@PathVariable Long id) {
        return userInfoRepo.findById(id).map(UserDTO::new).map(userDTOFactory::toModel)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * This method search for users with specific first name.
     * @param firstName of user.
     * @return CollectionModel of EntityModel of UserInfo.
     */
    @GetMapping("/user/byFirstName")
    @Operation(summary = "Get users by first name.")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByFirstName(@RequestParam("firstName") String firstName) {
        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findByFirstName(firstName).spliterator(), false)
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if (users.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for users with specific last name.
     * @param lastName of user
     * @return CollectionModel of EntityModel of UserInfo.
     */
    @GetMapping("/user/byLastName")
    @Operation(summary = "Get users by last name.")

    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByLastName(@RequestParam("lastName") Optional<String> lastName) {
        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findByLastName(lastName).spliterator(), false)
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if (users.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(BookInfoController.class)
                    .getAllBooks()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method search for user with a specific email address.
     * @param email of specific user
     * @return UserInfo.
     */
    @GetMapping("/user/byEmail/{email}")
    @Operation(summary = "Get user by email.")
    public ResponseEntity<UserInfo> getUserByEmail(@PathVariable("email") String email) {
        return Optional.ofNullable(userInfoRepo.findByEmail(email))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    /**
     * This method search for all users that was born between range of dates.
     * @param fromDate represents a lower threshold of dates.
     * @param toDate   represents an upper threshold of dates.
     * @return CollectionModel of EntityModel of UserInfo.
     * @throws Exception
     */
    @GetMapping("/users/birthdayDates/betweenDates")
    @Operation(summary = "Get all users that was born between range of dates.", description = "Please enter dates with format: yyyy-mm-dd ")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserBirthDateBetweenDates
    (@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
     @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception {

        //this is the format of the date we want to use(filter the date of birthday)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<EntityModel<UserInfo>> users = StreamSupport.stream(userInfoRepo.findAll().spliterator(), false)
                .filter(user -> {
                    try {
                        return (format.parse(user.getDateOfBirth()).getTime() >= fromDate.getTime() &&
                                format.parse(user.getDateOfBirth()).getTime() <= toDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(userEntityFactory::toModel).collect(Collectors.toList());
        if (users.size() != 0) {
            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(UserController.class)
                    .getAllUsers()).withSelfRel()));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method add a new user and save it in database.
     * @param userInfo represent a new user.
     * @return EntityModel of UserInfo.
     */
    @PostMapping("/users/add")
    @Operation(summary = "Add new user.")
    public ResponseEntity<EntityModel<UserInfo>> addUser(@Valid @RequestBody UserInfo userInfo) {

        UserInfo savedUser = userInfoRepo.save(userInfo);
        return ResponseEntity.created(linkTo(methodOn(UserController.class)
                        .getSpecificUser(savedUser.getId())).toUri())
                .body(userEntityFactory.toModel(savedUser));

    }

    /**
     * This method search for al users with specific first name and last name.
     * @param firstName of a user.
     * @param lastName  of a user.
     * @return CollectionModel of EntityModel of UserInfo.
     */
    @GetMapping("/users/byFullName")
    @Operation(summary = "Get all users by firstName + lastName.")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> getUserByFullName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
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

    /**
     * This method calculate the average age of all users
     * @return String.
     */
    @GetMapping("/users/averageAge")
    @Operation(summary = "Get average age of users.")
    public ResponseEntity<String> getAvgAge() {
        List<UserInfo> list = StreamSupport.stream(userInfoRepo.findAll().spliterator(), false).collect(Collectors.toList());
        int sum = 0, age = 0;
        for (int i = 0; i < list.size(); i++) {
            age = CURRYEAR - Integer.parseInt(((list.get(i).getDateOfBirth())).substring(0, 4));
            sum += age;
        }
        return ResponseEntity.ok("The average age of users is  " + sum / (list.size()));
    }

    /**
     * This method calculate the median age of all users.
     * @return String.
     */
    @GetMapping("/users/medianAge")
    @Operation(summary = "Get median age of users.")
    public ResponseEntity<String> getMedianAge() {
        List<UserInfo> list = StreamSupport.stream(userInfoRepo.findAll().spliterator(), false).collect(Collectors.toList());
        int ages[] = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ages[i] = CURRYEAR - Integer.parseInt(((list.get(i).getDateOfBirth())).substring(0, 4));
        }
        Arrays.sort(ages);
        if (ages.length % 2 == 0) return  ResponseEntity.ok("The median age of users is:"+ (((double) ages[ages.length / 2] + (double) ages[ages.length / 2 - 1]) / 2));
        else
            return ResponseEntity.ok("The median age of users is:"+(double) ages[ages.length / 2]);

    }
}
