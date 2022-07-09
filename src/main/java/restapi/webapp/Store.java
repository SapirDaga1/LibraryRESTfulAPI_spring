package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

/**
 * This class represents our Data Base.
 */

@Configuration
public class Store {
    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    @Bean
    CommandLineRunner initDataBase(BookInfoRepo bookInfoRepo, BooksOrderrRepo booksOrderRepo) {
        return args->{
            logger.info("logging " + bookInfoRepo.save
                    (new BookInfo(1L,5, "The beauty and the beast", "some description....", 10)));
            logger.info("logging " + bookInfoRepo.save
                    (new BookInfo(2L,15, "The little prince", "some other description....", 2)));
//            logger.info("logging "+ booksOrderRepo.save
//                    (new BooksOrderr(1,new HashSet<BookInfo>((new BookInfo(3L,33,"The devil wear Prada","wow",3),
//                            new BookInfo(4L,3,"The devil","bbbbb",3))))));
        };
    }


}
