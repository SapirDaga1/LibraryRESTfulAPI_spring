package restapi.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BooksOrderrRepo extends CrudRepository<BooksOrderr,Long> {
    //TODO: add 3 methods
    List<BooksOrderr> findByDateOfOrderr(String date);
    List<BooksOrderr> findByCityOfDelivery(String city);


}
