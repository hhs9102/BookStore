package me.ham.test;

import com.sun.org.glassfish.gmbal.Description;
import me.ham.Main;
import me.ham.exception.SoldOutException;
import me.ham.order.OrderService;
import me.ham.order.OrderServiceImpl;
import me.ham.pay.Card;
import me.ham.product.Book;
import me.ham.service.BookService;
import me.ham.service.BookServiceImpl;
import me.ham.store.BookStore;
import me.ham.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookStoreTest {

    private BookStore bookStore;
    private BookService bookService;
    private OrderService orderService;

    @BeforeAll
    public void setUp(){
        bookStore = new BookStore(new BigDecimal(5000));
        orderService = new OrderServiceImpl(new Card(), bookStore, new ReentrantLock());
        bookService = new BookServiceImpl(orderService, bookStore);

        ConcurrentHashMap<Integer, Book> concurrentHashMap = Main.initialBookMap();
        bookStore.setBookInfo(concurrentHashMap);
    }

    @Test
    @Description("정상 구매 테스트")
    public void normalPurchase(){
        //given
        User user = new User(1);
        bookService.addBook(user, 16374, new BigDecimal(10));
        bookService.addBook(user, 9236, new BigDecimal(10));
        BigDecimal orderPrice = user.getOrderPrice(bookStore);

        //when
        bookService.order(user);

        //then
        assertTrue(new BigDecimal(1618500).equals(orderPrice));
    }

    @Test
    @Description("CLASS 타입의 book 두번 구매")
    public void classTypePurchaseDuplication(){
        //given
        User user = new User(1);
        bookService.addBook(user, 16374, new BigDecimal(10));
        bookService.addBook(user, 9236, new BigDecimal(10));
        BigDecimal orderPrice = user.getOrderPrice(bookStore);
        bookService.order(user);    //1번 구매

        bookService.addBook(user, 16374, new BigDecimal(10));

        //then
        assertThrows(RuntimeException.class,()->{
            //when
            bookService.order(user);    //2번 구매
        });
    }

    @Test
    @Description("Thread start를 이용한 멀티스레스 테스트")
    public void multiThread() throws InterruptedException {
        //TODO exception 호출이 되지만 각각의 스레드가 별도로 실행되는지, 체크가 되지 않음.
        assertThrows(SoldOutException.class,
                () -> {
                    new Thread(() -> {
                        User user = new User(1);
                        bookService.addBook(user, 16374, new BigDecimal(5));
                        bookService.addBook(user,9236, new BigDecimal(10));
                        bookService.order(user);

                    }).start();
                    new Thread(() -> {

                        User user = new User(2);
                        bookService.addBook(user, 16374, new BigDecimal(5));
                        bookService.addBook(user,9236, new BigDecimal(10));
                        bookService.order(user);
                    }).start();
                    new Thread(() -> {
                        User user = new User(3);
                        bookService.addBook(user, 16374, new BigDecimal(5));
                        bookService.addBook(user,9236, new BigDecimal(10));
                        bookService.order(user);
                    }).start();
                });
    }

    @Test
    @Description("ExecutorService를 이용한 multi Thread 테스트")
    //9236의 재고가 22개라서 3번째 주문에서 SoldOutException
    public void multiThreadByExecutorService() throws InterruptedException {
        AtomicBoolean isSetException = new AtomicBoolean(false);

        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int nThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            for(int i=0; i<3; i++){
                executorService.submit(
                    ()->{
                        User user = new User(atomicInteger.getAndAdd(1));
                        bookService.addBook(user, 16374, new BigDecimal(5));
                        bookService.addBook(user,9236, new BigDecimal(10));

                        try{
                            bookService.order(user);
                            countDownLatch.countDown();
                        }catch (SoldOutException e){
                            countDownLatch.countDown();
                            isSetException.set(true);
                        }
                    }
                );
            }
        countDownLatch.await();
        assertTrue(isSetException.get());
    }
}
