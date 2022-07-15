package restapi.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface BooksOrderrRepo extends CrudRepository<BooksOrderr,Long> {
    //TODO: add 3 methods

}
