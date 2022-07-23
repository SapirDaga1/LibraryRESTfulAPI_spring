package restapi.webapp.assemblers;

import org.springframework.stereotype.Component;
import restapi.webapp.entities.UserInfo;
import restapi.webapp.controllers.UserController;

@Component
// HATEOAS factory that converts UserInfo objects into EntityModel<UserInfo> objects.
public class UserEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<UserInfo> {
    public UserEntityAssembler() {
        super(UserController.class);
    }
}

