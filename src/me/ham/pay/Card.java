package me.ham.pay;

import me.ham.Util;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.text.DecimalFormat;

public class Card implements Payment {

    @Override
    public boolean pay(User user, BookStore bookStore) {
        bookStore.checkStock(user.getPurchaseBooks()); //validation
        bookStore.updateStock(user.getPurchaseBooks()); //구매

        Util.printDash();

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        System.out.printf("지불금액 : %s원%n", decimalFormat.format(user.getOrderTotalAmount(bookStore)));
        return true;
    }
}
