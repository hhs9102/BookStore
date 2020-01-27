package me.ham.pay;

import me.ham.store.BookStore;
import me.ham.user.User;

public interface Payment {
    boolean pay(User user, BookStore bookStore);
}
