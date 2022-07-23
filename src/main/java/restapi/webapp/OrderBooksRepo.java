package restapi.webapp;

import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderBooksRepo extends CrudRepository<OrderBooks, Long> {
    //TODO: add 3 methods
    List<OrderBooks> findByDateOfOrder(String date);

    List<OrderBooks> findByCityOfDelivery(String city);

    List<OrderBooks> findByPrice(int price);


}
