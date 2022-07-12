package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.pojo.UserInfo;
import restapi.webapp.controllers.UserController;

@Component
// HATEOAS factory that converts UserInfo objects into EntityModel<UserInfo> objects.
public class UserEntityFactory extends SimpleIdentifiableRepresentationModelAssembler<UserInfo> {
    public UserEntityFactory(){super(UserController.class);}
}

