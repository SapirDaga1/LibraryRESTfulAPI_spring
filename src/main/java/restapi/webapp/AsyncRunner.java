package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncRunner implements CommandLineRunner {
    private final UserService userService;
    private static final Logger classLogger = LoggerFactory.getLogger(AsyncRunner.class);


    public AsyncRunner(UserService userService){
        this.userService=userService;
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

        CompletableFuture.allOf(bookInfo1,bookInfo2,bookInfo3).join();

//        classLogger.info("bookInfo1 = " + bookInfo1.get());
//        classLogger.info("bookInfo2 = " + bookInfo2.get());
//        classLogger.info("bookInfo3 = " + bookInfo3.get());


    }
}
