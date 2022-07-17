package restapi.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderBooksRepo extends CrudRepository<OrderBooks,Long> {
    //TODO: add 3 methods
    //This method return null..
    List<OrderBooks> findByDateOfOrderr(String date);
    List<OrderBooks> findByCityOfDelivery(String city);




}
