package restapi.webapp.assemblers;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojo.BooksOrderr;

public interface BooksOrderrRepo extends CrudRepository<BooksOrderr,Long> {
    //TODO: add 3 methods
}
