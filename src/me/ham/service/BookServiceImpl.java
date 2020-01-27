package me.ham.service;

import me.ham.order.OrderService;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.math.BigDecimal;

public class BookServiceImpl implements BookService {

    private OrderService orderService;

    private BookStore bookStore;

    public BookServiceImpl(OrderService orderService, BookStore bookStore) {
        this.orderService = orderService;
        this.bookStore = bookStore;
    }

    @Override
    public void addBook(User user, Integer bookNo, BigDecimal purchaseNumber) {
        user.getPurchaseMap().compute(bookNo
                , (key, value) -> {
                    return value == null ? purchaseNumber : value.add(purchaseNumber);
                });
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
        user.printOrderList(bookStore);
        user.printOrderPrice(bookStore);
    }
}
