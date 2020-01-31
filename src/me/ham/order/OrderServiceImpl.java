package me.ham.order;

import me.ham.pay.Payment;
import me.ham.store.BookStore;
import me.ham.user.User;

import java.util.concurrent.locks.ReentrantLock;

public class OrderServiceImpl implements OrderService{
    private Payment payment;
    private BookStore bookStore;
    private ReentrantLock reentrantLock;

    public OrderServiceImpl(Payment payment,BookStore bookStore, ReentrantLock reentrantLock) {
        this.payment = payment;
        this.bookStore = bookStore;
        this.reentrantLock = reentrantLock;
    }

    @Override
    public boolean order(User user){
        //TODO : LOCK 위치 고민 필요
        reentrantLock.lock();
        payment.pay(user, bookStore);
        postOrder(user);
        reentrantLock.unlock();
        return true;
    }

    @Override
    public void postOrder(User user) { //구매 후처리
        user.addPurchaseHistoryBook();
        user.clearPurchaseBooks();
    }
}
