package restapi.webapp.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import restapi.webapp.assemblers.BookInfoRepo;
import restapi.webapp.assemblers.BooksOrderrRepo;
import restapi.webapp.assemblers.UserInfoRepo;

import java.util.*;

/**
 * This class represents our Data Base.
 */

@Configuration
public class Store {
    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    @Bean
    CommandLineRunner initDataBase(BookInfoRepo bookInfoRepo, BooksOrderrRepo booksOrderRepo, UserInfoRepo userInfoRepo) {
        BookInfo bookInfo1= new BookInfo("LbnwCQAAQBAJ","Harry Potter","McFarland","2015-06-11",150);
        BookInfo bookInfo2= new BookInfo("LbnwCQAAQBAJ","Harry Potter5","McFarland","2017-06-11",450);
        return args->{
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("SapirDaga@gmail.com","Sapir","Daga","0501234567")));
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("RotemBT@gmail.com","Rotem","Ben-Tulila","0521471447")));

            logger.info("logging " + booksOrderRepo.save(new BooksOrderr(22L, new HashSet<BookInfo>(Arrays.asList(bookInfo1, bookInfo2)) {
            })));

        };
    }


}
