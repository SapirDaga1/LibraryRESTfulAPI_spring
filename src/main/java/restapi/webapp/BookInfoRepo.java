package restapi.webapp;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This class is a Data Access Layer (DAL).
 * Basic CRUD functionality is implemented according to the specific DB.
 */
public interface BookInfoRepo extends JpaRepository <BookInfo,Long>{
}
