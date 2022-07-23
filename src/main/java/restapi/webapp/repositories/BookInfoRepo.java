package restapi.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import restapi.webapp.entities.BookInfo;

import java.util.List;

/**
 * This class is a Data Access Layer (DAL).
 * Basic CRUD functionality is implemented according to the specific DB.
 */
public interface BookInfoRepo extends JpaRepository<BookInfo, Long> {

    BookInfo findByTitle(String title);

    BookInfo findByPageCount(int pages);

    List<BookInfo> findByPublisher(String publisher);
}
