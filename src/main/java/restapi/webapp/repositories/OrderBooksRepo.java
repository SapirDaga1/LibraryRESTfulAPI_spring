package restapi.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.entities.OrderBooks;

import java.util.List;

public interface OrderBooksRepo extends CrudRepository<OrderBooks, Long> {

    List<OrderBooks> findByDateOfOrder(String date);

    List<OrderBooks> findByCityOfDelivery(String city);

    List<OrderBooks> findByPrice(int price);


}
