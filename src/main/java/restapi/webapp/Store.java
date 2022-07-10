package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents our Data Base.
 */
//TODO: think if this class necessary.

@Configuration
public class Store {
    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    @Bean
    CommandLineRunner initDataBase(BookInfoRepo bookInfoRepo, BooksOrderrRepo booksOrderRepo,UserInfoRepo userInfoRepo) {
        return args->{
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("SapirDaga@gmail.com","Sapir","Daga","0501234567")));
            logger.info("logging " + userInfoRepo.save
                    (new UserInfo("RotemBT@gmail.com","Rotem","Ben-Tulila","0521471447")));
        };
    }


}
