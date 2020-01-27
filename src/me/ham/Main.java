package me.ham;

import me.ham.order.OrderService;
import me.ham.order.OrderServiceImpl;
import me.ham.pay.Card;
import me.ham.product.Book;
import me.ham.product.Type;
import me.ham.service.BookService;
import me.ham.service.BookServiceImpl;
import me.ham.store.BookStore;
import me.ham.user.User;

import javax.print.attribute.HashAttributeSet;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static ConcurrentHashMap<Integer, Book> bookMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        BookStore bookStore = new BookStore(new BigDecimal(5000));
        bookMap = initialBookMap();
        bookStore.setBookInfo(bookMap);

        ReentrantLock reentrantLock = new ReentrantLock();

        OrderService orderService = new OrderServiceImpl(new Card(), bookStore, new ReentrantLock()); ;
        BookService bookService = new BookServiceImpl(orderService, bookStore);

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
    }

    public static ConcurrentHashMap<Integer, Book> initialBookMap() {
        ConcurrentHashMap<Integer, Book> bookMap = new ConcurrentHashMap<>();
        List<Book> bookList = Arrays.asList(
                new Book(16374, Type.CLASS,"스마트스토어로 월 100만원",  new BigDecimal(151950), new BigDecimal(99999))
                ,new Book(26825, Type.CLASS,"해금, 특별하고 아름다운 나만의 반려악기",  new BigDecimal(114500), new BigDecimal(99999))
                ,new Book(91008, Type.KIT,"작고 쉽게 그려요",  new BigDecimal(28000), new BigDecimal(10))
                ,new Book(9236, Type.KIT,"하루의 시작과 끝",  new BigDecimal(9900), new BigDecimal(22))
        );

        for(Book book : bookList){
            bookMap.put(book.getNo(), book);
        }
        return bookMap;
    }
}
