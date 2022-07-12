package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts UserInfo objects into EntityModel<UserInfo> objects.
public class UserEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<UserInfo>{
    public UserEntityFactory(){super(UserController.class);}
}

