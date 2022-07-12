package restapi.webapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOFactory implements SimpleRepresentationModelAssembler<UserDTO> {

    @Override
    public void addLinks(EntityModel<UserDTO> resource) {
        resource.add(linkTo(methodOn(UserController.class).getSpecificUser(resource.getContent().getUser().getId())).withRel("single user"));
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all users information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {
        resources.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @Override
    public EntityModel<UserDTO> toModel(UserDTO user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getSpecificUser(user.getUser().getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("back all users"));
    }

}
