package me.ham;

import me.ham.order.Order;
import me.ham.pay.Card;
import me.ham.pay.Payment;
import me.ham.product.Book;
import me.ham.product.Type;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//        String readString = scanner.nextLine();
//        while(readString!=null) {
//            System.out.println(readString);
//
//            if (readString.isEmpty()) {
//                System.out.println("Read Enter Key.");
//            }else if("\\s".equals(readString)){
//                System.out.println("space");
//            }
//
//            if (scanner.hasNextLine()) {
//                readString = scanner.nextLine();
//            } else if("complete".equals(readString)){
//                readString = null;
//            }
//        }

        BookStore bookStore = new BookStore(new BigDecimal(5000));
        ConcurrentHashMap<Integer, Book> bookMap = new ConcurrentHashMap<>();
        initialBookMap(bookMap);
        bookStore.setBookMap(bookMap);

        ReentrantLock reentrantLock = new ReentrantLock();

        new Thread(() -> {
            User user = new User(1);
            user.purchaseBook(bookMap.get(16374), new BigDecimal(5));
            user.purchaseBook(bookMap.get(9236), new BigDecimal(10));

            user.getOrderList(bookStore);
            user.getOrderPrice(bookStore);

            Order order = new Order(bookStore);
            order.setPayment(new Card());
            order.setReentrantLock(reentrantLock);
            order.order(user);

        }).start();
        new Thread(() -> {

            User user = new User(2);
            user.purchaseBook(bookMap.get(16374), new BigDecimal(5));
            user.purchaseBook(bookMap.get(9236), new BigDecimal(10));

            user.getOrderList(bookStore);
            user.getOrderPrice(bookStore);

            Order order = new Order(bookStore);
            order.setPayment(new Card());
            order.setReentrantLock(reentrantLock);
            order.order(user);
        }).start();
        new Thread(() -> {

            User user = new User(3);
            user.purchaseBook(bookMap.get(16374), new BigDecimal(5));
            user.purchaseBook(bookMap.get(9236), new BigDecimal(10));

            user.getOrderList(bookStore);
            user.getOrderPrice(bookStore);

            Order order = new Order(bookStore);
            order.setPayment(new Card());
            order.setReentrantLock(reentrantLock);
            order.order(user);
        }).start();
    }

    private static void initialBookMap(Map<Integer, Book> bookMap) {
        List<Book> bookList = Arrays.asList(
                new Book(16374, Type.CLASS,"스마트스토어로 월 100만원",  new BigDecimal(151950), new BigDecimal(99999))
                ,new Book(26825, Type.CLASS,"해금, 특별하고 아름다운 나만의 반려악기",  new BigDecimal(114500), new BigDecimal(99999))
                ,new Book(91008, Type.KIT,"작고 쉽게 그려요",  new BigDecimal(28000), new BigDecimal(10))
                ,new Book(9236, Type.KIT,"하루의 시작과 끝",  new BigDecimal(9900), new BigDecimal(22))
        );

        for(Book book : bookList){
            bookMap.put(book.getNo(), book);
        }
    }
}
