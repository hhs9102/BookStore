package me.ham.service;

import me.ham.Util;
import me.ham.order.OrderService;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

public class BookServiceImpl implements BookService {

    private OrderService orderService;

    private BookStore bookStore;

    public BookServiceImpl(OrderService orderService, BookStore bookStore) {
        this.orderService = orderService;
        this.bookStore = bookStore;
    }
    @Override
    public void order(User user) {
        if(user.purchaseValidation(bookStore) && bookStore.purchaseValidation(user)){
            printOrderInfo(user);
            orderService.order(user);
            user.addPurchaseHistoryBook();
        }else{
            return;
        }
    }

    private void printOrderInfo(User user){
        printOrderList(user);
        printOrderTotalAmount(user);
    }

    public void printOrderList(User user){
        System.out.println("주문 내역:");
        Util.printDash();
        for(Map.Entry<Integer, BigDecimal> book :  user.getPurchaseBooks().entrySet()){
            System.out.printf("%s - %s개%n",bookStore.getBookInfo().get(book.getKey()).getName(), book.getValue().toString());
        }
    }

    //책 가격
    public void printOrderTotalAmount(User user){
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        BigDecimal amount = user.getOrderPrice(bookStore);

        Util.printDash();
        System.out.printf("주문금액: %s원%n", decimalFormat.format(amount));
        if(!bookStore.isFreeShips(amount)){
            amount.add(bookStore.getDeliveryPrice());
            System.out.printf("배송비: %s원%n", decimalFormat.format(bookStore.getDeliveryPrice()));
        }
    }
}
