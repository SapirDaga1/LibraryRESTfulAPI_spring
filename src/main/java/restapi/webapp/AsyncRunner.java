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

    public AsyncRunner(UserService userService, BookInfoRepo bookInfoRepo) {
        this.userService = userService;
        this.bookInfoRepo = bookInfoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture<BookInfo> bookInfo1 = userService.getDataFromApi("Report");
        CompletableFuture<BookInfo> bookInfo2 = userService.getDataFromApi("Minnesota Rules of Court");
        CompletableFuture<BookInfo> bookInfo3 = userService.getDataFromApi("The Psychology of Harry Potter");
        CompletableFuture<BookInfo> bookInfo4 = userService.getDataFromApi("Common Stocks and Common Sense");
        CompletableFuture<BookInfo> bookInfo5 = userService.getDataFromApi("Getting Rich with Low-priced Stocks");
        CompletableFuture<BookInfo> bookInfo6 = userService.getDataFromApi("Profits from Penny Stocks");

        CompletableFuture<BookInfo>[] taskArray = new CompletableFuture[6];

        taskArray[0] = bookInfo1;
        taskArray[1] = bookInfo2;
        taskArray[2] = bookInfo3;
        taskArray[3] = bookInfo4;
        taskArray[4] = bookInfo5;
        taskArray[5] = bookInfo6;
        CompletableFuture.allOf(taskArray);

        BookInfo newBookInfo1 = bookInfo1.get();
        BookInfo newBookInfo2 = bookInfo2.get();
        BookInfo newBookInfo3 = bookInfo3.get();
        BookInfo newBookInfo4 = bookInfo4.get();
        BookInfo newBookInfo5 = bookInfo5.get();
        BookInfo newBookInfo6 = bookInfo6.get();

        CompletableFuture.allOf(bookInfo1, bookInfo2, bookInfo3, bookInfo4, bookInfo5, bookInfo6).join();
        bookInfoRepo.save(newBookInfo1);
        bookInfoRepo.save(newBookInfo2);
        bookInfoRepo.save(newBookInfo3);
        bookInfoRepo.save(newBookInfo4);
        bookInfoRepo.save(newBookInfo5);
        bookInfoRepo.save(newBookInfo6);

        classLogger.info("bookInfo1 = " + bookInfo1.get());
        classLogger.info("bookInfo2 = " + bookInfo2.get());
        classLogger.info("bookInfo3 = " + bookInfo3.get());
        classLogger.info("bookInfo4 = " + bookInfo4.get());
        classLogger.info("bookInfo5 = " + bookInfo5.get());
        classLogger.info("bookInfo6 = " + bookInfo6.get());

    }
}
