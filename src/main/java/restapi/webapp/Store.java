package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents our Data Base.
 */

@Configuration
public class Store {
    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    @Bean
    CommandLineRunner initDataBase(BookInfoRepo bookInfoRepo, BooksOrderrRepo booksOrderRepo,UserInfoRepo userInfoRepo) {

        BookInfo bookInfo1= new BookInfo("LbnwCQAAQBAJ","Harry Potter","McFarland","2015-06-11",150);
        BookInfo bookInfo2= new BookInfo("LbnwCQAAQBAJ","Harry Potter5","McFarland","2017-06-11",450);
        bookInfoRepo.save(bookInfo1);
        bookInfoRepo.save(bookInfo2);
        return args->{
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("SapirDaga@gmail.com","Sapir","Daga","0501234567")));
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("RotemBT@gmail.com","Rotem","Ben-Tulila","0521471447")));

            logger.info("logging " + booksOrderRepo.save
                    (new BooksOrderr(new ArrayList<>(Arrays.asList(bookInfo1,bookInfo2)),
                           userInfoRepo.save(new UserInfo("natan@gmail.com","natan","dillbary","0505666666")) )));


        };
    }


}
