package restapi.webapp;

import org.springframework.stereotype.Component;

@Component
// HATEOAS factory that converts UserInfo objects into EntityModel<UserInfo> objects.
public class UserEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<UserInfo>{
    public UserEntityAssembler(){super(UserController.class);}
}

