package me.ham.order;

import me.ham.pay.Payment;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.util.concurrent.locks.ReentrantLock;

public class Order {
    Payment payment;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    private BookStore bookStore;

    public Order(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    ReentrantLock reentrantLock;

    public void setReentrantLock(ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
    }

    public boolean order(User user){
        reentrantLock.lock();
        payment.pay(user, bookStore);
        user.addPurchaseHistoryBook(bookStore);
        reentrantLock.unlock();
        return true;
    }
}
