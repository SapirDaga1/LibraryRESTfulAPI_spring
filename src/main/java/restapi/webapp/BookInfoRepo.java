package restapi.webapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This class is a Data Access Layer (DAL).
 * Basic CRUD functionality is implemented according to the specific DB.
 */
public interface BookInfoRepo extends JpaRepository <BookInfo,Long>{
    //TODO: add 3 methods
    ResponseEntity<EntityModel<BookDTO>> findByTitle(String title);
    ResponseEntity<EntityModel<BookDTO>> findByPublishedDate(String date);



}
