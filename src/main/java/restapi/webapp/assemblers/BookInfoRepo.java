package restapi.webapp.assemblers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import restapi.webapp.pojo.BookInfo;
import restapi.webapp.dto.BookDTO;

/**
 * This class is a Data Access Layer (DAL).
 * Basic CRUD functionality is implemented according to the specific DB.
 */
public interface BookInfoRepo extends JpaRepository <BookInfo,Long>{

    BookInfo findByTitle(String title);
    BookInfo findByPageCount(int pages);
    List<BookInfo> findByPublisher(String publisher);
}
