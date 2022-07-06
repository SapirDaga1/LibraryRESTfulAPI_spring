package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

public interface BooksOrderRepo extends CrudRepository<BooksOrder,Long> {
}
