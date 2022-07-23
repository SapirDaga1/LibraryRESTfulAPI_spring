package restapi.webapp.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restapi.webapp.entities.BookInfo;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    // RestTemplate is used to invoke an external REST point by another service
    private RestTemplate restTemplate;


    /*
     RestTemplateBuilder is used by Spring to supply default configuration to the
     RestTemplate instance, specifically MessageConvertors
     */
    public UserService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * This method should run asynchronously in order to get
     * information about a specific book - using @Async annotation and CompletableFuture
     * Our program needs to send an HTTP GET request to a remote REST endpoint
     * we ought to get a JSON representing the book.
     *
     * @param title
     * @return
     */
    @Async
    public CompletableFuture<BookInfo> getDataFromApi(String title) {
        String urlTemplate = String.format("https://www.googleapis.com/books/v1/volumes?q=%s", title);
        BookInfo bookInfo = this.restTemplate.getForObject(urlTemplate, BookInfo.class);
        bookInfo.getTitle();
        /*
         return a CompletableFuture<BookInfo> when the computation is done
         this goes hand-with-hand with the join() method
         */

        return CompletableFuture.completedFuture(bookInfo);
    }
}
