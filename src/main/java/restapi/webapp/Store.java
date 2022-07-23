package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * This class represents our Data Base.
 */

@Configuration
public class Store {
    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    @Bean
    CommandLineRunner initDataBase(BookInfoRepo bookInfoRepo, OrderBooksRepo booksOrderRepo, UserInfoRepo userInfoRepo) {

        BookInfo bookInfo1 = new BookInfo("LbnwCQAAQBAJ", "Harry Potter", "McFarland", "2015-06-11", 150, "en", "0.2.2.0.preview.0");
        BookInfo bookInfo2 = new BookInfo("LbnwCQAAQBAJ", "Harry Potter5", "McFarland", "2017-06-11", 450, "en", "0.1.2.0.preview.0");
        bookInfoRepo.save(bookInfo1);
        bookInfoRepo.save(bookInfo2);
        BookInfo bookInfo3 = new BookInfo("CMBYDwAAQBAJ", "Black Beauty", "Anna Sewell", "1999-06-23", 186, "en", "0.1.0.0.preview.1");
        BookInfo bookInfo4 = new BookInfo("HUpKDAAAQBAJ", "A Novel in Linocuts", "Courier Dover Publications", "2016-09-21", 144, "en", "0.2.0.0.preview.1");
        BookInfo bookInfo5 = new BookInfo("3HWQAgAAQBAJ", "Serious Games", "Routledge", "2009-09-10", 552, "en", "1.1.2.0.preview.3");
        BookInfo bookInfo6 = new BookInfo("8tTVDwAAQBAJ", "Disney Princess: Book of Secrets", "Studio Fun International", "2020-03-10", 44, "en", "preview-1.0.0");
        bookInfoRepo.save(bookInfo3);
        bookInfoRepo.save(bookInfo4);
        bookInfoRepo.save(bookInfo5);
        bookInfoRepo.save(bookInfo6);
        return args -> {
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("SapirDaga@gmail.com", "Sapir", "Daga", "0501234567", "Tel-Aviv", "1997-02-24")));
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("RotemBT@gmail.com", "Rotem", "Ben-Tulila", "0521471447", "Ashdod", "1996-01-15")));
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("RotemCo@gmail.com", "Rotem", "Cohen", "0507897897", "Rishon-Le Zion", "2002-03-01")));

            logger.info("logging " + booksOrderRepo.save
                    (new OrderBooks(new ArrayList<>(Arrays.asList(bookInfo1, bookInfo2)), "2022-5-12", "Bat-Yam",
                            userInfoRepo.save(new UserInfo("natan@gmail.com", "natan", "dillbary", "0505666666", "Bat-Yam", "1985-08-28")), 150)));
            logger.info("logging " + booksOrderRepo.save
                    (new OrderBooks(new ArrayList<>(Arrays.asList(bookInfo3, bookInfo4, bookInfo5, bookInfo6)), "2022-05-24", "Tel-Aviv",
                            userInfoRepo.save(new UserInfo("helen@gmail.com", "Helen", "Cohen", "0520505051", "Ashkelon", "1991-06-29")), 200)));
            logger.info("logging " + booksOrderRepo.save
                    (new OrderBooks(new ArrayList<>(Arrays.asList(bookInfo1, bookInfo3, bookInfo6)), "2022-02-24", "Ashkelon",
                            userInfoRepo.save(new UserInfo("noa@gmail.com", "Noa", "Cohen", "0544449999", "Eilat", "1968-11-16")), 165)));
            logger.info("logging " + booksOrderRepo.save
                    (new OrderBooks(new ArrayList<>(Arrays.asList(bookInfo2, bookInfo3, bookInfo6)), "2022-07-24", "Tel-Aviv",
                            userInfoRepo.save(new UserInfo("noa@gmail.com", "Noa", "Cohen", "0544449999", "Eilat", "1968-11-16")), 195)));

        };
    }


}
