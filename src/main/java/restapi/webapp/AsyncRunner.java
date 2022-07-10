package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
public class AsyncRunner implements CommandLineRunner {
    private final UserService userService;
    private static final Logger classLogger = LoggerFactory.getLogger(AsyncRunner.class);
    private BookInfoRepo bookInfoRepo;


    public AsyncRunner(UserService userService,BookInfoRepo bookInfoRepo){
        this.userService=userService;
        this.bookInfoRepo=bookInfoRepo;
    }
    @Override
    public void run(String... args) throws Exception {
        CompletableFuture<BookInfo> bookInfo1 = userService.bookInf("LbnwCQAAQBAJ");
        CompletableFuture<BookInfo> bookInfo2 = userService.bookInf("TMS8tQEACAAJ");
        CompletableFuture<BookInfo> bookInfo3 = userService.bookInf("dt4HAQAAMAAJ");

        CompletableFuture<BookInfo>[] taskArray =  new CompletableFuture[3];

        taskArray[0] = bookInfo1;
        taskArray[1] = bookInfo2;
        taskArray[2] = bookInfo3;
        CompletableFuture.allOf(taskArray);

        BookInfo newBookInfo1 = bookInfo1.get();
        BookInfo newBookInfo2 = bookInfo2.get();
        BookInfo newBookInfo3 = bookInfo3.get();

        CompletableFuture.allOf(bookInfo1,bookInfo2,bookInfo3).join();
        bookInfoRepo.save(newBookInfo1);
        bookInfoRepo.save(newBookInfo2);
        bookInfoRepo.save(newBookInfo3);

        classLogger.info("bookInfo1 = " + bookInfo1.get());
        classLogger.info("bookInfo2 = " + bookInfo2.get());
        classLogger.info("bookInfo3 = " + bookInfo3.get());


    }
}
